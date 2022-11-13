package com.example.aakash.ui.home;

public class HomePostFilterModel {

    private int filterPosition;
    private String filterTitle;

    public HomePostFilterModel() {
    }

    public HomePostFilterModel(int filterPosition, String filterTitle) {
        this.filterPosition = filterPosition;
        this.filterTitle = filterTitle;
    }

    public int getFilterPosition() {
        return filterPosition;
    }

    public void setFilterPosition(int filterPosition) {
        this.filterPosition = filterPosition;
    }

    public String getFilterTitle() {
        return filterTitle;
    }

    public void setFilterTitle(String filterTitle) {
        this.filterTitle = filterTitle;
    }
}
