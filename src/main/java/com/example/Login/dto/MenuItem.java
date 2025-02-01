package com.example.Login.dto;

public class MenuItem {
    private String name;
    private String link;
    private String icon;

    public MenuItem(String name, String link, String icon) {
        this.name = name;
        this.link = link;
        this.icon = icon;
    }

    public String getName() { return name; }
    public String getLink() { return link; }
    public String getIcon() { return icon; }
}
