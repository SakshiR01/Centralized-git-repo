package com.gemini.Contripoint.model;

import com.sun.istack.Nullable;
import org.springframework.web.multipart.MultipartFile;

public class EventWrapper {

    private String eventDetails;
    @Nullable
    private MultipartFile carousel;
    @Nullable
    private MultipartFile banner;

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    @Nullable
    public MultipartFile getCarousel() {
        return carousel;
    }

    public void setCarousel(@Nullable MultipartFile carousel) {
        this.carousel = carousel;
    }

    @Nullable
    public MultipartFile getBanner() {
        return banner;
    }

    public void setBanner(@Nullable MultipartFile banner) {
        this.banner = banner;
    }
}
