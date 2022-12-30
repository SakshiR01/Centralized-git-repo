package com.gemini.Contripoint.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
public class Count {
    int certification;
    int clientFeedback;
    int interview;
    int trainingSessions;
    int mentorship;
    int projects;
    int teamBuildingActivity;
    int selfDevelopmentActivity;

    public Count(int certification, int clientFeedback, int interview, int trainingSessions, int mentorship, int projects, int teamBuildingActivity, int selfDevelopmentActivity) {
        this.certification = certification;
        this.clientFeedback = clientFeedback;
        this.interview = interview;
        this.trainingSessions = trainingSessions;
        this.mentorship = mentorship;
        this.projects = projects;
        this.teamBuildingActivity = teamBuildingActivity;
        this.selfDevelopmentActivity = selfDevelopmentActivity;
    }

    public int getCertification() {
        return certification;
    }

    public int getClientFeedback() {
        return clientFeedback;
    }

    public int getInterview() {
        return interview;
    }

    public int getTrainingSessions() {
        return trainingSessions;
    }

    public int getMentorship() {
        return mentorship;
    }

    public int getProjects() {
        return projects;
    }

    public int getTeamBuildingActivity() {
        return teamBuildingActivity;
    }

    public int getSelfDevelopmentActivity() {
        return selfDevelopmentActivity;
    }
}
