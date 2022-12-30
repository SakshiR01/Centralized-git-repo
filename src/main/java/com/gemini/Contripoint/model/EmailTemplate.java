package com.gemini.Contripoint.model;

import java.util.List;

public class EmailTemplate {

    private String managerName;
    private int contributionsPending;
    private List<ContributionCount> contributionCountList;

    public EmailTemplate(String managerName, int contributionsPending, List<ContributionCount> contributionCountList) {
        this.managerName = managerName;
        this.contributionsPending = contributionsPending;
        this.contributionCountList = contributionCountList;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public int getContributionsPending() {
        return contributionsPending;
    }

    public void setContributionsPending(int contributionsPending) {
        this.contributionsPending = contributionsPending;
    }

    public List<ContributionCount> getContributionCountList() {
        return contributionCountList;
    }

    public void setContributionCountList(List<ContributionCount> contributionCountList) {
        this.contributionCountList = contributionCountList;
    }
}
