# [QuickShopX](https://github.com/innc11/QuickShopX)

一个适用于Nukkit的插件,QuickShopX,由原作者[WetABQ](https://github.com/WetABQ)的插件修改而来

 A plugin for Nukkit, QuickShopX,original author [WetABQ](https://github.com/WetABQ)

## 相对于QuickShop的变化

#### - 创建商店的变化

创建商店由自己放置牌子和右键交互事件改为只有左键交互事件（就是和Bukkit上的QuickShop一样,左键点击一下,就是轻微的破坏一点点,在创造模式下也能很好的工作

#### - 商店保护机制的变化

增加了商店的保护机制,包括爆炸、活塞、燃烧做了相应的处理,不会在出现箱子消失的情况了

#### - 商店配置文件的变化

商店的商店配置文件沿用了QuickShop的设计,并作出了一些小小的更改,其中common字段是方便调试用的,插件不会读取它

#### - 全息物品的变化

完善全息物品（商店箱子上方的悬浮物品）显示的问题,在有多个世界的情况下也能很好的工作,并将发包操作放到了异步线程里进行

#### - 商店牌子文字的变化

商店的牌子上的文字可以自由修改了（通过signText.yml文件）,同样支持彩色代码

#### - 商店牌子的变化

增加了商店牌子实时显示商店库存等信息（包括往箱子里面放物品的时候也会实时更新牌子数据）,对使用漏斗机械来作为自动补货机制的商店同样有效,牌子会实时更新

#### - 自定义物品名称的变化

插件现在自带了中文物品名的config文件（插件自带的是Java版的物品命名,目前只提供了JE命名,如果觉得不满意还烦请手动修调）,找不到自定义物品名的情况下才会使用Nukkit自带的英文物品名称

#### - 插件API的变化

比如QuickShop的PlayerBuyEvent可以获取到Player对象和ShopData对象,但QuickShopX的PlayerBuyEvent是可以获取Player对象和BuyShop对象,由BuyShop对象可以获取到ShopData对象,这一点在PlayerBuyEvent上同理,需要注意

#### - 语言文件的变化

由QuickShop的{}占位符变化成${ITEM_NAME}、${TARGET_COUNT}占位符,现在可以自由调整占位符的前后顺序了,几乎所有能见到的文字都可以自定义

#### - 工作机制的变化

1. 对商店的操作增加了FORM界面的支持,当然如果愿意可以使用chat进行操作,这个可以由配置文件控制触发方式,或是一直使用chat操作,或者一直使用form操作,或是单击牌子使用chat操作,双击使用form操作
2. 对插件本身的配置也由form接管,使用/qs cp可以打开插件的控制面板(默认OP)具体内容可以看图片（包括针对Residence插件的交互,对全息物品的控制,对op的权限控制等）
3. 当Residence插件存在的时候并启用了与Residence插件交互时,领地内有build权限和container权限的人可以破坏商店和打开商店箱子但无法修改商店的信息（商店信息只能由商店主人来修改,且主人如果没有build权限是无法破坏商店的,但一般不会出现这个情况）

## 尚未完善的地方 Not perfect
1. ~~语言文件目前只提供了中文,因为工作量太大没能顾及英文语言~~(1.2.5版本修复)
2. ~~每个商店箱子的角落方向上的4个位置上不能出现另一个商店的箱子,下个版本会修复~~(1.2.3版本修复)
3. ~~\不在领地内创建商店可能会导致物品被窃走,请务必在领地内创建商店,此功能可以被配置启动或者关闭~~(1.2.5版本修复)
4. ~~Win10右键可能会多次触发Interaction事件~~(MC在1.13版本中已经修复这个问题)

## 图片预览 Picture Preview

图片加载可能比较缓慢或者直接404,如果不能显示请打开images文件夹下载后查看

![overview](https://res.innc11.cn/pictures/quickshopx/Overview.png)
![p1](https://res.innc11.cn/pictures/quickshopx/Trading.png)
![p2](https://res.innc11.cn/pictures/quickshopx/Blocked.png)
![p3](https://res.innc11.cn/pictures/quickshopx/ControlPanel.png)

## 更新记录 Change logs

#### 1.0.1

1. 修改插件控制面板标题中显示的配置文件版本为插件版本
2. 添加商店破坏条件：是否在潜行下才能破坏,这个功能可以由配置文件进行控制
3. 优化部分默认语言文本
4. 添加本README文件

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

#### 1.2

1. 移除了店主面板里面的"移除商店"按钮
2. 修复了插件事件无法触发的问题
3. 增加了对漏斗补货机制中牌子文字显示不更新的问题
4. 添加了对非商店主人或在领地内时没有build权限的人的商店箱子保护机制（不让他们打开）
5. 调整了修改商店主人的功能只能由OP使用
6. 修改"控制面板"中的"全息物品每秒最大发包量"的最大值由500提升到800
7. 增加了针对没有打开商店箱子权限时的提示（在语言文件里）

#### 1.2.1

1. 修复了在领地内时有build权限却提示没有build权限而导致无法打开商店箱子的问题
2. 修复了plugin.yml文件中插件版本显示不正确的问题

#### 1.2.2
1. 修复了交易面板和店主面板Title显示不正确的问题

#### 1.2.3
1. 修复了取消了交易后等待交易数量输入的过程不会超时的问题(会导致把与商店交互后的第一条聊天信息作为交易数量处理)
2. 修复了对NBT物品的判断
3. 店主现在可以通过聊天栏来与自己的商店交易了(但不会有文字提示,但仍然可以交易)
4. 完善部分带有meta的物品命名显示(需要删除itemName.yml文件然后重新生成)
5. 修复大箱子报错的问题
6. 最大每秒发包由800提升至1000

#### 1.2.4
1. 修改默认的config.yml
2. 修复在没有安装Residence就打开商店时的报错问题

#### 1.2.5
1. 漏斗补货在领地外默认被禁用,防止他人使用漏斗窃取物品,领地内可以正常使用没有限制,可以由config.yml配置
2. 新增了'使用自定义物品命名'配置项,默认开启,开启会使用itemName.yml中的命名,关闭会使用Nukkit的命名
3. 添加了英文语言文件（language.yml和signText.yml）
4. 这个版本更新需要删除language.yml让其从新生成
5. 修复语言文件无法被/qs reload重载的问题

#### 1.3
1. 支持NBT(附魔, Lore, CustomName)特性(物品上的CustomName不受"使用自定义物品命名"的影响,仍然会被显示,且会把原有的名字代替)
2. ~~对出售商店的悬浮物增加了堆叠的机制（当物品多余1个的时候会有堆叠的效果）~~(有点难看,已删除)
3. 修复always模式下商店牌子无法破坏的问题
4. 修复在"交互超时"时间以内创建商店会一起触发与上一次的商店的购买行为
5. 这个版本更新需要删除language.yml让其从新生成(记得备份,修改了 IM_SHOP_INFO_SHOW, FORM_TRADING__SHOP_INFO, FORM_MASTER__CONTENT, 以及新增了部分语言(在文件开头))
6. 修改/qs unlimited指令变为/qs server,功能不变
7. 修改美化signText.yml默认配色

#### 1.3.1
1. 添加部分具有Damage值的物品的中文命名(需要删除itemName.yml并重启服务器来生效)

#### 1.4
1. 美化与QuickShop同时加载时的提示
2. 修复商店x轴坐标小于0时玩家登录卡住然后被踢出的问题
3. 美化部分__中文__语言文件(需要删除language.yml并重启服务器)
4. 修复一个潜在的bug

#### 1.4.1
1. 美化控制面板的布局(调试按钮不要打开)
2. 彻底修复商店x轴坐标小于0时玩家登录卡住然后被踢出的问题(采用配置文件存储随机生成的id)
3. 美化配置文件的common字段带有颜色符号的问题
4. 去除一些代码注释

#### 1.5
1. 修正英文语言中4个单词语法问题
2. 调整控制面板布局
3. 所有的语言相关配置文件大幅重构(shops.yml除外),不与以前版本兼容
4. 删除所有语言相关配置文件占位符中$符号,直接使用{var}形式
5. 调整商店文件(shops.yml),根据世界进行拆分由文件夹进行分类(插件启动时会自动转换)
6. 新增控制面板中的语言设置项目,可以快速切换语言
7. 默认不再附带物品命名文件(itemNames.yml)如有需要可手动下载
8. 需要手动删除enchantments.yml/language.yml/signTexts.yml三个文件
9. 商店主人可以正常与自己的商店进行交易
10. 移除支持大箱子的计划,但依旧不影响当成两个小箱子使用
11. 修复英文语言文件中UI界面里的文字和中文语言不一致的问题
12. 完善"附魔命名"的英文版(跟随Java版命名)

## 指令 Commands

| 指令                        | 描述                   | 权限   |
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
