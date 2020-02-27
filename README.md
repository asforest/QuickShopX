# [QuickShopX](https://github.com/innc11/QuickShopX)

一个适用于Nukkit的插件,QuickShopX,由原作者[WetABQ](https://github.com/WetABQ)的插件修改而来

 A plugin for Nukkit, QuickShopX,original author [WetABQ](https://github.com/WetABQ)

## 尚未完善的地方 Not perfect
1. ~~语言文件目前只提供了中文,因为工作量太大没能顾及英文语言~~(1.2.5版本修复)
2. ~~每个商店箱子的角落方向上的4个位置上不能出现另一个商店的箱子,下个版本会修复~~(1.2.3版本修复)
3. ~~\不在领地内创建商店可能会导致物品被窃走,请务必在领地内创建商店,此功能可以被配置启动或者关闭~~(1.2.5版本修复)
4. ~~Win10右键可能会多次触发Interaction事件~~(MC在1.13版本中已经修复这个问题)

## 图片预览 Picture Preview

![overview](https://res.innc11.cn/pictures/quickshopx/Overview.png)
![p1](https://res.innc11.cn/pictures/quickshopx/Trading.png)
![p2](https://res.innc11.cn/pictures/quickshopx/Blocked.png)
![p3](https://res.innc11.cn/pictures/quickshopx/ControlPanel.png)
![p4](https://res.innc11.cn/pictures/quickshopx/Commands.png)

## 更新记录 Change logs

#### 1.0.1

1. 修改插件控制面板标题中显示的配置文件版本为插件版本
2. 添加商店破坏条件：是否在潜行下才能破坏,这个功能可以由配置文件进行控制
3. 优化部分默认语言文本
4. 添加本README文件
---
1. Modify the plug-in control panel title
2. Add a shop break condition: whether it can be broken by snake mode is controlled by the configuration file
3. Optimize some default language text
4. Add this README file

#### 1.1

1. 优化语言配置文件读取机制,缺少的语言会自动补齐
2. 添加可监听的插件事件
	- PlayerBuyEvent（玩家购买事件）
	- PlayerSellEvent（玩家出售事件）
	- PlayerCreateShopEvent（玩家创建商店事件）
	- PlayerRemoveShopEvent（玩家破坏商店事件）
3. 交换商店店主页面的"打开商店设置面板"和"打开商店交易面板"的按钮的位置
4. 优化所有金额的显示格式
5. 修正"干海带"物品名称显示成"海泡菜"的问题
6. 修复聊天栏中输入价格时无法输入小数的问题
7. 添加配置文件中也会写入物品名称,用于辅助调试
8. 修改"系统商店"的概念为"服务器商店"
9. 修正一处语言文件的错误
10. 修复出售商店最大交易量计算不正确的问题
11. 修改商店主人显示信息
12. 修改"控制面板"中的"全息物品每秒最大发包量"的最大值由100提升到500
---
1. Optimized language configuration file reading mechanism, missing languages will be automatically filled in
2. Add plugin events
	- PlayerBuyEvent
	- PlayerSellEvent
	- PlayerCreateShopEvent
	- PlayerRemoveShopEvent
3. Swap the buttons for "Open Store Settings Panel" and "Open Store Transaction Panel" in the shop home panel
4. optimize the display format of all money
5. Fixed an issue where "Dried Kelp" item names were displayed as "Kimchi"
6. Fixed the issue that the decimal cannot be entered when entering the price in the chat bar
7. The item name is also written in the configuration file to assist debugging
8. Modify the concept of "System Shop" to "Server Shop"
9. Fix a language file error
10. Fix the problem that the maximum transaction volume of the shop is not calculated correctly
11. Modify shop owner display information
12. Changed the maximum value of "Max Hologram Items package send Per Second" in "Control Panel" from 100 to 500

#### 1.2

1. 移除了店主面板里面的"移除商店"按钮
2. 修复了插件事件无法触发的问题
3. 修复了对漏斗补货机制中牌子文字显示不更新的问题
4. 添加了对非商店主人或在领地内时没有build权限的人的商店箱子保护机制（不让他们打开）
5. 调整了修改商店主人的功能只能由OP使用
6. 修改"控制面板"中的"全息物品每秒最大发包量"的最大值由500提升到800
7. 增加了针对没有打开商店箱子权限时的提示（在语言文件里）
---
1. Removed "Remove Shop" button in the owner panel
2. Fixed an issue where plugin events could not be triggered
3. Fixed the issue that the sign text is not updated
4. Added shop chest protection for non-shop owners or people who do not have build permissions while in the residence (do not let them open)
5. Adjusted the function to modify the shop owner can only be used by the OP
6. Changed the maximum value of "Max Hologram Items package send Per Second" in Control Panel from 500 to 800
7. Added a reminder when the shop chest permissions are not opened (in the language file)

#### 1.2.1

1. 修复了在领地内时有build权限却提示没有build权限而导致无法打开商店箱子的问题
2. 修复了plugin.yml文件中插件版本显示不正确的问题
---
1. Fixed an issue where there was a build permission in the residence but it showed that there was no build permission and the shop chest could not be opened
2. Fixed an issue where the plugin version was displayed incorrectly in the plugin.yml file

#### 1.2.2
1. 修复了交易面板和店主面板Title显示不正确的问题
---
1. Fixed the issue where the title of the transaction panel and the owner panel was incorrect

#### 1.2.3
1. 修复了取消了交易后等待交易数量输入的过程不会超时的问题(会导致把与商店交互后的第一条聊天信息作为交易数量处理)
2. 修复了对NBT物品的判断
3. 店主现在可以通过聊天栏来与自己的商店交易了(但不会有文字提示,但仍然可以交易)
4. 完善部分带有meta的物品命名显示(需要删除itemName.yml文件然后重新生成)
5. 修复大箱子报错的问题
6. 最大每秒发包由800提升至1000
---
1. Fixed the issue that the process of waiting for the transaction number input after canceling the transaction would not time out (which would cause the first chat message after interacting with the store to be treated as the transaction number)
2. Fixed judgment on NBT items
3. Owners can now use the chat bar to trade with their own shop (but there will be no text prompts, but they can still trade)
4. Improve the naming display of some items with meta (need to delete the itemName.yml file and regenerate)
5. Fixed double chest error

#### 1.2.4
1. Modify the default config.yml
2. 修复在没有安装Residence就打开商店时的报错问题
---
1. 修改默认的config.yml
2. Fix the error when opening the store without installing Residence plugin

#### 1.2.5
1. 漏斗补货在领地外默认被禁用,防止他人使用漏斗窃取物品,领地内可以正常使用没有限制,可以由config.yml配置
2. 新增了'使用自定义物品命名'配置项,默认开启,开启会使用itemName.yml中的命名,关闭会使用Nukkit的命名
3. 添加了英文语言文件（language.yml和signText.yml）
4. 这个版本更新需要删除language.yml让其从新生成
5. 修复语言文件无法被/qs reload重载的问题
---
1. Hopper replenishment is disabled by default outside the residence to prevent others from using the funnel to steal items. There are no restrictions on normal use in the residence, which can be configured by config.yml
2. Added the 'Use custom item naming' configuration item. It is enabled by default. The name in itemName.yml will be used when it is turned on.
3. Added English language files (language.yml and signText.yml)
4. This version update needs to delete language.yml to make it new
5. Fix the problem that language files cannot be reloaded by "/ qs reload"

#### 1.3
1. 支持NBT(附魔, Lore, CustomName)特性(物品上的CustomName不受"使用自定义物品命名"的影响,仍然会被显示,且会把原有的名字代替)
2. ~~对出售商店的全系物品增加了堆叠的机制（当物品多余1个的时候会有堆叠的效果）~~(有点难看,已删除)
3. 修复always模式下商店牌子无法破坏的问题
4. 修复在"交互超时"时间以内创建商店会一起触发与上一次的商店的购买行为
5. 这个版本更新需要删除language.yml让其从新生成(记得备份,修改了 IM_SHOP_INFO_SHOW, FORM_TRADING__SHOP_INFO, FORM_MASTER__CONTENT, 以及新增了部分语言(在文件开头))
6. 修改/qs unlimited指令变为/qs server,功能不变
7. 修改美化signText.yml默认配色
---
1. Support NBT (Enchantment, Lore, CustomName) feature (CustomName on the item is not affected by "Use custom item name", it will still be displayed and the original name will be replaced)
2. ~~Added a stacking mechanism to all items in the shop (when there is more than one item, it will have a stacking effect)~~(a bit ugly, deleted)
3. Fixed the problem that the shop sign cannot be destroyed in "always mode"
4. Fixed the issue of creating a shop within the "interaction timeout" time will trigger the purchase behavior with the previous shop
5. This version update needs to delete language.yml to generate it again (remember to back up, modify IM_SHOP_INFO_SHOW, FORM_TRADING__SHOP_INFO, FORM_MASTER__CONTENT, and add some languages (at the beginning of the file))
6. Modify the "/ qs unlimited" command to "/ qs server", the function remains unchanged
7. Modify the default color of landscaping signText.yml

#### 1.3.1
1. 添加部分具有Damage值的物品的中文命名(需要删除itemName.yml并重启服务器来生效)
---
1. Add Chinese name for some items with Damage value (requires itemName.yml and restart the server to take effect)

#### 1.4
1. 美化与QuickShop同时加载时的提示
2. 修复商店x轴坐标小于0时玩家登录卡住然后被踢出的问题
3. 美化部分中文语言文件(需要删除language.yml并重启服务器)
4. 修复一个潜在的bug
---
1. Beautification tips when loading with QuickShop at the same time
2. Fixed an issue where players logged in and got kicked out when the x-coordinate of the shop was less than 0
3. Beautify the Chinese language files (requires deleting language.yml and restarting the server)
4. Fix a potential bug

#### 1.4.1
1. 美化控制面板的布局(调试按钮不要打开)
2. 彻底修复商店x轴坐标小于0时玩家登录卡住然后被踢出的问题(采用配置文件存储随机生成的id)
3. 美化配置文件的common字段带有颜色符号的问题
4. 去除一些代码注释
---
1. Beautify the layout of the control panel (do not open the debug button)
2. Completely fix the problem that the player logs in and gets kicked out when the x-axis coordinate of the store is less than 0 (using a configuration file to store the randomly generated id)
3. Beautify the common field of the configuration file with color symbols
4. Remove some code comments

#### 1.5
1. 修正英文语言中4个单词问题和语法问题
2. 调整控制面板布局
3. 所有的语言相关配置文件大幅重构(shops.yml除外),不与以前版本兼容
4. 删除所有语言相关配置文件占位符中$符号,直接使用{placeholder}形式
5. 调整商店文件(shops.yml),根据世界进行拆分由文件夹进行分类(插件启动时会自动转换)
7. 默认不再附带物品命名文件(itemNames.yml)如有需要可手动下载
8. 需要手动删除enchantments.yml/language.yml/signTexts.yml三个文件
9. 商店主人可以正常与自己的商店进行交易
10. 移除支持大箱子的计划,但依旧不影响当成两个小箱子使用
11. 修复英文语言文件中UI界面里的表达和中文语言不一致的问题
12. 完善"附魔命名"的英文版(跟随Java版命名)
13. "自定义物品命名"现在默认关闭,且不再自动生成,和改名为custom-names.yml,如果需要请手动放到插件文件夹里
14. config.yml文件进行了重构,并且增加了语言选项
---
1. Fixed 4 words and grammar problems in English language
2. Adjust the control panel layout
3. All language-related configuration files are significantly refactored (except shops.yml), which is not compatible with previous versions
4. Remove the $ sign from the placeholders of all language-related configuration files, and use the {placeholder} form directly
5. Adjust the store file (shops.yml), split according to the world, and categorize by folder (the plugin will automatically convert when the plugin starts)
7. Item name files (itemNames.yml) are no longer attached by default and can be manually downloaded if necessary
8. You need to manually delete the three files: invitations.yml / language.yml / signTexts.yml
9. Shop owners can conduct transactions with their own stores normally
10. Removed support for double chest, but still does not affect use as two small boxes
11. Fix the inconsistency between the expression in the UI interface in the English language file and the Chinese language
12. Improve the English version of "Enchantment Naming" (follow the Java Edition)
13. "Custom Item Naming" is now turned off by default, and is no longer automatically generated, and renamed to custom-names.yml, if necessary, please manually put it in the plugin folder
14. config.yml file has been refactored and language options have been added

#### 1.5.1
1. 支持用权限节点来限制商店个数(格式: quickshopx.create.<数量>),不使用则不限制数量
2. 修复在创建商店时,如果手上为空会创建一个空牌子的问题
---
1. Support the use of permission nodes to limit the number of shops (format: quickshopx.create.<Number>), if not used, there is no limit to the number of shops
2. Fixed the problem of creating an empty sign if the hand is empty when creating a shop

#### 1.5.2
1. 现在同时支持打掉箱子和打掉牌子来破坏商店
2. item-names.yml可以被/qs r重新加载,不用再重启插件就可以直接加载
3. 优化一些代码逻辑
---
1. Now supports both break the chest and break the sign to destroy the shop
2. The item-names.yml can be reload by /qs r, it does not need to reload the plugin itself
2. Optimize some code logic

## 指令 Commands

| 指令commands                | 描述description        | 权限permission   |
| --------------------------- | ---------------------- | ------ |
| /qs help(h)                 | 显示帮助信息           | player |
| /qs buy(b)                  | 改变为购买类型的商店   | player |
| /qs sell(s)                 | 改变为出售类型的商店   | player |
| /qs price(p) <price>        | 改变商店的交易价格     | player |
| /qs server(se)              | 改变商店为系统商店     | op     |
| /qs version(v)              | 显示插件的版本信息     | op     |
| /qs controlpanel(cp)        | 显示插件的控制面板     | op     |
| /qs reload(r)               | 重新加载插件的配置文件 | op     |

## API

```java
//Listening event(PlayerBuyEvent,PlayerSellEvent,PlayerCreateShopEvent,PlayerRemoveShopEvent)

@EventHandler
public void onPlayerBuyEvent(PlayerBuyEvent event) 
{
    event.getPlayer().sendMessage(event.getShop().getStringPrice()+"\n"+event.getCount());
}
```

## 作者 Authors
- [innc11](https://github.com/innc11)
