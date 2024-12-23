package com.pwing.shops.skript.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.pwing.shops.api.PwingShopsAPI;
import org.bukkit.event.Event;

public class ExprShopPrice extends SimpleExpression<Number> {
    private Expression<String> itemId;
    private Expression<String> shopId;

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected Number[] get(Event e) {
        String item = itemId.getSingle(e);
        String shop = shopId.getSingle(e);
        if (item == null || shop == null) return new Number[0];
        
        return new Number[]{PwingShopsAPI.getShop(shop).getItems().values().stream()
            .filter(i -> i.getId().equals(item))
            .findFirst()
            .map(i -> i.getCurrentPrice())
            .orElse(0.0)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }


    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        itemId = (Expression<String>) exprs[0];
        shopId = (Expression<String>) exprs[1];
        return true;
    }


    @Override
    public String toString(Event e, boolean debug) {
        return "price of " + itemId.toString(e, debug) + " in shop " + shopId.toString(e, debug);
    }
}