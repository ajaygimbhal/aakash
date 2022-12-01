package com.jadhav.aakash.ui.home;

public class HomePostFilterModel {

    private String filterTitle;
    private boolean selected;

    public HomePostFilterModel() {
    }

    public HomePostFilterModel(String filterTitle, boolean selected) {
        this.filterTitle = filterTitle;
        this.selected = selected;
    }

    public String getFilterTitle() {
        return filterTitle;
    }

    public void setFilterTitle(String filterTitle) {
        this.filterTitle = filterTitle;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
