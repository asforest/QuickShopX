#[QuickShop2](https://github.com/innc11/QuickShop2)

一个适用于Nukkit的插件，QuickShop2，由原作者[WetABQ](https://github.com/WetABQ)的插件修改而来

 A plugin for Nukkit, QuickShop2,original author [WetABQ](https://github.com/WetABQ)

##更新记录Change logs

#### 1.0.1

1. 修改插件控制面板标题中显示的配置文件版本为插件版本
2. 添加商店破坏条件：是否在潜行下才能破坏，这个功能可以由配置文件控制
3. 优化部分默认语言文本
4. 添加本README文件(｀・ω・´)

#### 1.1

1. 优化语言配置文件读取机制，缺少的语言会自动补齐
2. 添加可监听的插件事件
	- PlayerBuyEvent（玩家购买事件）
	
	- PlayerSellEvent（玩家出售事件）
	
	- PlayerCreateShopEvent（玩家创建商店事件）
	
	- PlayerRemoveShopEvent（玩家破坏商店事件）
3. 交换商店店主页面的"打开商店设置面板"和"打开商店交易面板"的按钮的位置
4. 优化所有金额的显示格式
5. 修正"干海带"物品名称显示成"海泡菜"的问题
6. 修复聊天栏中输入价格时无法输入小数的问题
7. 添加配置文件中也会写入物品名称，用于辅助调试
8. 修改"系统商店"的概念为"服务器商店"
9. 修正一处语言文件的错误
10. 修复出售商店最大交易量计算不正确的问题
11. 修改商店主人显示信息
12. 修改"控制面板"中的"全息物品每秒最大发包量"的最大值由100提升到500