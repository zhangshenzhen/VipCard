package com.bjypt.vipcard.widgets.videolist.visibility.scroll;


import com.bjypt.vipcard.widgets.videolist.visibility.items.ListItem;

/**
 *
 * @author Wayne
 */
public interface ItemsProvider {

    ListItem getListItem(int position);

    int listItemSize();

}
