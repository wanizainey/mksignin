package com.maryamkhadijah.mk;

import java.util.HashMap;
import java.util.Map;

public class LeaveApplication {
    private String leaveType;
    private String startDate;
    private String endDate;
    private String leaveReason;
    private boolean morningLeave;
    private boolean eveningLeave;

    private boolean isApproved; // Added field

    public LeaveApplication() {
        // Default constructor required for Firebase
    }

    public LeaveApplication(String leaveType, String startDate, String endDate, String leaveReason, boolean morningLeave, boolean eveningLeave) {
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveReason = leaveReason;
        this.morningLeave = morningLeave;
        this.eveningLeave = eveningLeave;
    }

    // Getter methods
    public String getLeaveType() {
        return leaveType;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public boolean isMorningLeave() {
        return morningLeave;
    }

    public boolean isEveningLeave() {
        return eveningLeave;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("leaveType", leaveType);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("leaveReason", leaveReason);
        result.put("morningLeave", morningLeave);
        result.put("eveningLeave", eveningLeave);

        return result;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

}
