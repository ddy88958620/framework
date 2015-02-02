package com.handu.apollo.rule.utils;

import com.handu.apollo.rule.RuleSnippet;

import java.util.Comparator;

public class ComparatorWeight implements Comparator<RuleSnippet> {
    public int compare(RuleSnippet o1, RuleSnippet o2) {
        return o1.getWeight().compareTo(o2.getWeight()) * -1;
    }
}