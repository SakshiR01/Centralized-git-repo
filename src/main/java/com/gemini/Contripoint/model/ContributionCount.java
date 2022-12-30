package com.gemini.Contripoint.model;

public class ContributionCount {
    private String contributionCategory;
    private int contributionCategorySize;

    public ContributionCount(String contributionCategory, int contributionCategorySize) {
        this.contributionCategory = contributionCategory;
        this.contributionCategorySize = contributionCategorySize;
    }

    public ContributionCount() {
        this.contributionCategory = null;
        this.contributionCategorySize = 0;
    }

    public String getContributionCategory() {
        return contributionCategory;
    }

    public void setContributionCategory(String contributionCategory) {
        this.contributionCategory = contributionCategory;
    }

    public int getContributionCategorySize() {
        return contributionCategorySize;
    }

    public void setContributionCategorySize(int contributionCategorySize) {
        this.contributionCategorySize = contributionCategorySize;
    }
}
