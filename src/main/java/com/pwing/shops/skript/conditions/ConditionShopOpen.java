package com.pwing.shops.skript.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.pwing.shops.api.PwingShopsAPI;
import org.bukkit.event.Event;

public class ConditionShopOpen extends Condition {
    private Expression<String> shopId;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        shopId = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        String id = shopId.getSingle(e);
        return id != null && PwingShopsAPI.getShop(id).isOpen();
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "shop " + shopId.toString(e, debug) + " is open";
    }
}
