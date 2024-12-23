---
title: Shop Configuration
layout: page
nav_order: 2
has_children: false
---

# Shop Configuration

## Basic Structure
```
display-name: "&b&lExample Shop"
command: "exampleshop"

# Shop opening hours
schedule:
  MONDAY:
    open: "09:00"
    close: "17:00"
  FRIDAY:
    open: "00:00"
    close: "23:59"
```

Items Configuration

```
items:
  diamond_sword:
    slot: 0
    item: "DIAMOND_SWORD"
    buy-price: 100
    sell-price: 50
    bulk-discounts:
      32: 0.1  # 10% discount for buying 32
      64: 0.2  # 20% discount for buying 64
    sale:
      discount: 0.15
      start: "2024-01-01 00:00"
      end: "2024-12-31 23:59"
    stock: 100
    max-stock: 200
    stock-regen:
      amount: 5
      interval: 300  # 5 minutes
```

Custom Items Support

```
items:
  custom_item:
    slot: 1
    item: "itemsadder:special_sword"  # ItemsAdder item
  another_item:
    slot: 2
    item: "oraxen:magic_staff"        # Oraxen item
```
Stock Management

``
    stock: Current stock amount (-1 for unlimited)
    max-stock: Maximum stock limit
    stock-regen: Automatic stock regeneration
        amount: Amount to regenerate
        interval: Time in seconds between regenerations
```

Sales System

    Time-based sales with configurable discounts
    Bulk purchase discounts
    Multiple currency support through PwingEco
# Currency Configuration

## PwingEco Integration
```yaml
currencies:
  coins:
    buy-price: 100
    sell-price: 80
  gems:
    buy-price: 10
    sell-price: 8
  tokens:
    buy-price: 5
    sell-price: 4

currencies.md
Item-Specific Currency Prices

items:
  special_sword:
    slot: 13
    item: "DIAMOND_SWORD"
    name: "&b&lMulti-Currency Sword"
    lore:
      - "&7Buy with different currencies:"
      - "&6• %pwingeco_coins% Coins"
      - "&b• %pwingeco_gems% Gems"
      - "&e• %pwingeco_tokens% Tokens"
    currencies:
      coins:
        buy: 500
        sell: 400
      gems:
        buy: 50
        sell: 40
      tokens:
        buy: 25
        sell: 20

# Currency Configuration

## PwingEco Integration
```yaml
currencies:
  coins:
    buy-price: 100
    sell-price: 80
  gems:
    buy-price: 10
    sell-price: 8
  tokens:
    buy-price: 5
    sell-price: 4

currencies.md
Item-Specific Currency Prices

items:
  special_sword:
    slot: 13
    item: "DIAMOND_SWORD"
    name: "&b&lMulti-Currency Sword"
    lore:
      - "&7Buy with different currencies:"
      - "&6• %pwingeco_coins% Coins"
      - "&b• %pwingeco_gems% Gems"
      - "&e• %pwingeco_tokens% Tokens"
    currencies:
      coins:
        buy: 500
        sell: 400
      gems:
        buy: 50
        sell: 40
      tokens:
        buy: 25
        sell: 20

# Currency Configuration

## PwingEco Integration
```
currencies:
  coins:
    buy-price: 100
    sell-price: 80
  gems:
    buy-price: 10
    sell-price: 8
  tokens:
    buy-price: 5
    sell-price: 4
```

Item-Specific Currency Prices

```
items:
  special_sword:
    slot: 13
    item: "DIAMOND_SWORD"
    name: "&b&lMulti-Currency Sword"
    lore:
      - "&7Buy with different currencies:"
      - "&6• %pwingeco_coins% Coins"
      - "&b• %pwingeco_gems% Gems"
      - "&e• %pwingeco_tokens% Tokens"
    currencies:
      coins:
        buy: 500
        sell: 400
      gems:
        buy: 50
        sell: 40
      tokens:
        buy: 25
        sell: 20
```


# Menu Configuration

## Basic Structure
```
title: "&e&lExample Menu"
size: 54  # 6 rows

items:
  welcome:
    slot: 13
    item: "NETHER_STAR"
    name: "&b&lWelcome!"
    lore:
      - "&7Welcome to the example menu"
      - "&7Your balance: &e%vault_eco_balance%"
    actions:
      - "[sound] ENTITY_EXPERIENCE_ORB_PICKUP 1 1"
      - "[message] &aWelcome to our shop!"
``
Available Actions
```
    [message] - Send message to player
    [broadcast] - Broadcast message to server
    [sound] - Play sound
    [player] - Execute player command
    [console] - Execute console command
    [takemoney] - Remove money from player
    [givemoney] - Give money to player
    [takeexp] - Remove experience
    [giveexp] - Give experience
    [givepermission] - Grant permission
    [takepermission] - Remove permission
```
PlaceholderAPI Support

All text fields support PlaceholderAPI placeholders for dynamic content.
Color Codes

    Traditional color codes (&a, &b, etc.)
    RGB Hex colors (#FF0000, #00FF00)

#Default Currency Fallback

If PwingEco is not installed, the plugin will automatically fall back to Vault economy.