package com.paycraft.ticketbooker.models;

public class ConfirmBooking {

    private boolean success;
    private String ticketId;

    public ConfirmBooking(boolean success, String ticketId) {
        this.success = success;
        this.ticketId = ticketId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    @Override
    public String toString() {
        return "ConfirmBooking{" +
                "success=" + success +
                ", ticketId='" + ticketId + '\'' +
                '}';
    }
}

