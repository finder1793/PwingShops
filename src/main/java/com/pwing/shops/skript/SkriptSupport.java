package com.pwing.shops.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionType;
import com.pwing.shops.PwingShops;
import com.pwing.shops.skript.conditions.ConditionShopOpen;
import com.pwing.shops.skript.expressions.ExprShopPrice;
import com.pwing.shops.skript.effects.EffectOpenShop;

public class SkriptSupport {
    public static void register(PwingShops plugin) {
        // Shop Conditions
        Skript.registerCondition(ConditionShopOpen.class, "shop %string% is (open|closed)");
        
        // Shop Expressions
        Skript.registerExpression(ExprShopPrice.class, Number.class, ExpressionType.PROPERTY, 
            "price of %string% in shop %string%");
            
        // Shop Effects
        Skript.registerEffect(EffectOpenShop.class, "open shop %string% to %player%");
    }
}

