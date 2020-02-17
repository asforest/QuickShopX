package cn.innc11.quickshopx.utils;

public enum Lang
{
    buy,
    sell,
    server_shop_nickname,

    enchantment_text,
    enchantment_per_line,
    enchantment_per_prefix,
    enchantment_per_suffix,
    enchantment_per_prefix_first,
    enchantment_per_suffix_last,

    im_shop_type_updated,
    im_shop_type_donot_need_update,
    im_not_allow_modify_price_not_owner,
    im_interaction_timeout,
    im_not_selected_shop,
    im_intercept_console,
    im_shop_price_updated,
    im_price_wrong_format,
    im_price_wrong_args,
    im_shop_updated_server,
    im_shop_updated_ordinary,
    im_shop_info,
    im_enter_transactions_volume,
    im_not_a_number,
    im_successfully_removed_shop,
    im_not_allow_remove_shop_not_owner,
    im_not_allow_remove_shop_exists_sign,
    im_creating_shop_enter_price,
    im_successfully_created_shop,
    im_no_item_in_hand,
    im_shop_sign_blocked,
    im_trade_canceled,
    im_buyshop_owner,
    im_buyshop_customer,
    im_buyshop_backpack_full,
    im_buyshop_sold_out,
    im_buyshop_insufficient_stock,
    im_buyshop_not_enough_money,
    im_sellshop_not_enough_item,
    im_sellshop_not_enough_money,
    im_sellshop_owner,
    im_sellshop_customer,
    im_sellshop_stock_full,
    im_shop_data_updated,
    im_snake_mode_destroy_shop,
    im_no_residence_permission,
    im_create_shop_in_residence_only,
    im_not_allow_cross_residence,
    im_sign_not_in_residence,
    im_not_allow_sign_in_another_residence,
    im_not_allow_others_open_chest,


    shopdata_title,
    shopdata_price,
    shopdata_owner,
    shopdata_type,
    shopdata_server_shop,

    trading_title,
    trading_shop_info,
    trading_trading_volume,

    owner_title,
    owner_content,
    owner_button_shop_data_panel,
    owner_button_shop_trading_panel,

    cp_title,
    cp_interaction_time,
    cp_hologram_item,
    cp_interaction_way,
    cp_packet_send_ps,
    cp_link_with_residence,
    cp_create_in_residence_only,
    cp_op_ignore_build_permission,
    cp_snake_mode_destroy_shop,
    cp_hopper_limit,
    cp_use_custom_item_name,
    cp_debug,
    cp_tax,

    plugin_message_reload_done,
    plugin_message_reload_failed,
    plugin_message_configure_updated,
    plugin_message_help_normal,
    plugin_message_help_operator;

    public String getDefaultLangText()
    {
        return name().replace("_", "-");
    }

    public static boolean contains(String value)
    {
        for(Lang lang : values())
        {
            if(lang.name().equals(value))
                return true;
        }
        return false;
    }
}
