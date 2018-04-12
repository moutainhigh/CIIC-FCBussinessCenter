package com.ciicsh.caldispatchjob.compute.util;

import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

import java.util.Set;

/**
 * Created by bill on 18/1/17.
 */
public class CustomAgendaFilter implements AgendaFilter {

    private final Set<String> ruleNamesThatAreAllowedToFire;//传入的rule name

    public CustomAgendaFilter(Set<String> ruleNamesThatAreAllowedToFire) {
        this.ruleNamesThatAreAllowedToFire = ruleNamesThatAreAllowedToFire;
    }
    @Override
    public boolean accept(Match match) {
        return ruleNamesThatAreAllowedToFire.contains(match.getRule().getName());
    }

}
