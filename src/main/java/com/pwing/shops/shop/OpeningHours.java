package com.pwing.shops.shop;

import java.time.LocalTime;

public class OpeningHours {
    private final LocalTime openTime;
    private final LocalTime closeTime;
    
    public OpeningHours(LocalTime openTime, LocalTime closeTime) {
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
    
    public LocalTime getOpenTime() { return openTime; }
    public LocalTime getCloseTime() { return closeTime; }
    
    public boolean isOpen(LocalTime currentTime) {
        return currentTime.isAfter(openTime) && currentTime.isBefore(closeTime);
    }
}