package com.pwing.shops.menu.action;

import com.pwing.shops.PwingShops;

@FunctionalInterface
public interface ActionFactory {
    MenuAction create(PwingShops plugin, String content);
}
