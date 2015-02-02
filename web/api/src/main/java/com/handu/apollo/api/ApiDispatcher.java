package com.handu.apollo.api;

import com.google.common.collect.Lists;
import com.handu.apollo.core.ApiConstants;
import com.handu.apollo.core.ApiErrorCode;
import com.handu.apollo.utils.DateUtil;
import com.handu.apollo.utils.Log;
import com.handu.apollo.utils.ReflectUtil;
import com.handu.apollo.utils.StringPool;
import com.handu.apollo.utils.exception.ApiException;
import com.handu.apollo.utils.exception.ApolloRuntimeException;
import com.handu.apollo.utils.exception.InvalidParameterValueException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by markerking on 14-4-8.
 */
public class ApiDispatcher {
    private static final Log LOG = Log.getLog(ApiDispatcher.class);

    public static void dispatchValidateCmd(BaseValidateCmd cmd, Map<String, String> params) throws Exception {
        processParameters(cmd, params);
        cmd.validate();
        cmd.execute();
    }

    public static void dispatch(BaseCmd cmd, Map<String, String> params) throws Exception {
        processParameters(cmd, params);
        cmd.execute();
    }

    public static void processParameters(BaseCmd cmd, Map<String, String> params) {
        Map<String, Object> unpackedParams = cmd.unpackParams(params);
        if (cmd instanceof BaseListCmd) {
            String pageStr = (String) unpackedParams.get(ApiConstants.PAGE);
            String pageSizeStr = (String) unpackedParams.get(ApiConstants.PAGE_SIZE);

            Integer pageSize = null;
            if (StringUtils.isNotBlank(pageSizeStr)) {
                pageSize = Integer.parseInt(pageSizeStr);
            }

            if ((pageSize != null && !pageSize.equals(BaseListCmd.PAGESIZE_UNLIMITED)) && pageStr == null) {
                throw new ApiException(ApiErrorCode.PARAMETER_VALIDATION_ERROR, "如果设置了pageSize参数，必须设置page参数");
            } else if (StringUtils.isNotBlank(pageStr) && pageSize == null) {
                throw new ApiException(ApiErrorCode.PARAMETER_VALIDATION_ERROR, "如果设置了page参数，必须设置pageSize参数");
            }
        }

        List<Field> fields = ReflectUtil.getAllFieldsForClass(cmd.getClass(), BaseCmd.class);

        for (Field field : fields) {
            Input parameterAnnotation = field.getAnnotation(Input.class);
            if ((parameterAnnotation == null) || !parameterAnnotation.expose()) {
                continue;
            }

            Object paramObj = unpackedParams.get(field.getName());
            if (paramObj == null) {
                if (parameterAnnotation.required()) {
                    throw new ApiException(ApiErrorCode.PARAM_ERROR, "无法执行API命令[" + cmd.getClass().getSimpleName() + "]，缺少必须的参数[ " + field.getName() + "]");
                }
                // HOTFIX_1.0.3: 因采用单例模式，为null的数据依然处理
                //continue;
            }

            // 验证字段数据类型，并设置参数值
            try {
                setFieldValue(field, cmd, paramObj, parameterAnnotation);
            } catch (IllegalArgumentException argEx) {
                LOG.debug("无法执行命令[" + cmd.getClass().getSimpleName() + "]，参数值[" + paramObj + "]，参数[" + field.getName() + "]");
                throw new ApiException(ApiErrorCode.PARAM_ERROR, "无法执行命令[" + cmd.getClass().getSimpleName() + "]，参数值[" + paramObj + "]，参数[" + field.getName() + "]");
            } catch (ParseException e) {
                LOG.debug("错误的日期参数[" + paramObj + "]传递给命令[" + cmd.getClass().getSimpleName() + "]");
                throw new ApiException(ApiErrorCode.PARAM_ERROR, "错误的日期参数[" + paramObj + "]传递给命令[" + cmd.getClass().getSimpleName() + "]，请查看文档中日期字段给出的格式");
            } catch (InvalidParameterValueException invEx) {
                LOG.error("InvalidParameterValueException", invEx);
                throw new ApiException(ApiErrorCode.PARAM_ERROR, "无法执行API命令[" + cmd.getClass().getSimpleName() + "]，" + invEx.getMessage());
            } catch (ApolloRuntimeException ex) {
                LOG.error("ApolloRuntimeException", ex);
                throw new ApiException(ApiErrorCode.INTERNAL_ERROR, "执行命令出现内部错误[" + cmd.getClass().getSimpleName() + "]");
            }
        }
    }

    private static void setFieldValue(Field field, BaseCmd cmdObj, Object paramObj, Input annotation) throws IllegalArgumentException, ParseException {
        try {
            field.setAccessible(true);
            BaseCmd.CommandType fieldType = annotation.type();
            if (paramObj == null || StringUtils.isBlank(paramObj.toString())) {
                ReflectUtil.setValueToDefault(field, cmdObj);
                return;
            }
            switch (fieldType) {
                case BOOLEAN:
                    field.set(cmdObj, Boolean.valueOf(paramObj.toString()));
                    break;
                case DATE:
                    field.set(cmdObj, DateUtil.parse(paramObj.toString()));
                    break;
                case FLOAT:
                    field.set(cmdObj, Float.valueOf(paramObj.toString()));
                    break;
                case DOUBLE:
                    field.set(cmdObj, Double.valueOf(paramObj.toString()));
                    break;
                case INTEGER:
                    field.set(cmdObj, Integer.valueOf(paramObj.toString()));
                    break;
                case LIST:
                    List listParam = Lists.newArrayList();
                    String[] sp = paramObj.toString().split(StringPool.COMMA, -1);
                    for (String token : sp) {
                        BaseCmd.CommandType listType = annotation.collectionType();
                        switch (listType) {
                            case INTEGER:
                                listParam.add(Integer.valueOf(token));
                                break;
                            case LONG:
                                listParam.add(Long.valueOf(token));
                                break;
                            case SHORT:
                                listParam.add(Short.valueOf(token));
                                break;
                            case STRING:
                                listParam.add(token);
                                break;
                            case FLOAT:
                                listParam.add(Float.valueOf(token));
                                break;
                            case DOUBLE:
                                listParam.add(Double.valueOf(token));
                                break;
                            case DATE:
                                listParam.add(DateUtil.parse(token));
                        }
                    }
                    field.set(cmdObj, listParam);
                    break;
                case LONG:
                    field.set(cmdObj, Long.valueOf(paramObj.toString()));
                    break;
                case SHORT:
                    field.set(cmdObj, Short.valueOf(paramObj.toString()));
                    break;
                case STRING:
                    if (paramObj.toString().length() > annotation.length()) {
                        LOG.error("字段[" + field.getName() + "]的值大于最大允许的长度[" + annotation.length() + "]");
                        throw new InvalidParameterValueException("字段[" + field.getName() + "]的值大于最大允许的长度[" + annotation.length() + "]");
                    }
                    try {
                        field.set(cmdObj, paramObj.toString());
                    } catch (Exception e) {
                        LOG.error("字段[" + field.getName() + "]设置值时出现异常", e);
                        throw new InvalidParameterValueException("字段[" + field.getName() + "]设置值时出现异常");
                    }
                    break;
                case MAP:
                default:
                    field.set(cmdObj, paramObj);
                    break;
            }
        } catch (IllegalAccessException ex) {
            LOG.error("命令[" + cmdObj.getClass().getSimpleName() + "]初始化出现内部错误，字段[ " + field.getName() + "]不可访问");
            throw new ApolloRuntimeException("命令[" + cmdObj.getClass().getSimpleName() + "]初始化出现内部错误，字段[ " + field.getName() + "]不可访问");
        }
    }
}