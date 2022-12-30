package com.gemini.Contripoint.model;

import org.springframework.data.domain.Page;

public class RewardResponse {

    private Integer eventId;
    private String eventName;
    private String reward;
    private Integer position;
    private Integer amount;
    private String rewardDate;

    public RewardResponse(Integer eventId, String eventName, String reward, Integer position, Integer amount) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.reward = reward;
        this.position = position;
        this.amount = amount;
    }

    public RewardResponse(Integer eventId, String eventName, String reward, Integer amount) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.reward = reward;
        this.amount = amount;
    }

    public RewardResponse(Page<String> eventRewards) {

    }

    public RewardResponse() {

    }

    public String getRewardDate() {
        return rewardDate;
    }

    public void setRewardDate(String rewardDate) {
        this.rewardDate = rewardDate;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
