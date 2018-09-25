package com.tweakbit.model;

import com.tweakbit.driverupdater.model.enties.VisitToTweakBit;

import java.util.TreeSet;

public class AUID {
    String auid;
    TreeSet<VisitToTweakBit> setOfVisits;
    boolean wasDownload;
    double revenue;

    public AUID(String auid, double revenue) {
        this.auid = auid;
        this.setOfVisits = new TreeSet<>();
        this.revenue = revenue;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public String getAuid() {
        return auid;
    }

    public void setAuid(String auid) {
        this.auid = auid;
    }

    public TreeSet<VisitToTweakBit> getSetOfVisits() {
        return setOfVisits;
    }

    public void setSetOfVisits(TreeSet<VisitToTweakBit> setOfVisits) {
        this.setOfVisits = setOfVisits;
    }

    public boolean isWasDownload() {
        return wasDownload;
    }

    public void setWasDownload(boolean wasDownload) {
        this.wasDownload = wasDownload;
    }
}
