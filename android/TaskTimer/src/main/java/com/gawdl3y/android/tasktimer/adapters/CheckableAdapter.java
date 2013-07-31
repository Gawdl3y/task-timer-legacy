package com.gawdl3y.android.tasktimer.adapters;

import android.util.SparseBooleanArray;
import android.widget.BaseAdapter;

/**
 * An adapter that can keep track of item checked states
 * <p/>
 * Note that if the data set is changed while items are checked, the positions will
 * not accurately reflect the new positions of the items.
 * @author Schuyler Cebulskie
 */
public abstract class CheckableAdapter extends BaseAdapter {
    private SparseBooleanArray mCheckedItems = new SparseBooleanArray();
    private int mNumCheckedItems = 0;

    /**
     * Toggles the checked state of the item at {@code position}
     * @param position The position of the item to toggle
     */
    public void toggleItem(int position) {
        mCheckedItems.put(position, !mCheckedItems.get(position));
        if(mCheckedItems.get(position)) mNumCheckedItems++; else mNumCheckedItems--;
    }

    /**
     * Sets whether or not the item at {@code position} is checked
     * @param position The position of the item
     * @param checked  Whether or not the item is checked
     */
    public void setItemChecked(int position, boolean checked) {
        if(checked && !mCheckedItems.get(position)) mNumCheckedItems++;
        else if(!checked && mCheckedItems.get(position)) mNumCheckedItems--;
        mCheckedItems.put(position, checked);
    }

    /**
     * Sets the checked state of all of the items
     * @param checked Whether or not the items are checked
     */
    public void setallItemsChecked(boolean checked) {
        for(int i = 0; i < getCount(); i++) {
            if(checked && !mCheckedItems.get(i)) mNumCheckedItems++;
            else if(!checked && mCheckedItems.get(i)) mNumCheckedItems--;
            mCheckedItems.put(i, checked);
        }
    }

    /**
     * @param position The position of the item to test
     * @return {@code true} if the item at {@code position} is checked, {@code false} if not
     */
    public boolean isItemChecked(int position) {
        return mCheckedItems.get(position);
    }

    /**
     * @return The number of checked items
     */
    public int getCheckedItemCount() {
        return mNumCheckedItems;
    }

    /**
     * @return An array of positions of all of the checked items
     */
    public int[] getCheckedItemPositions() {
        int[] positions = new int[mNumCheckedItems];
        int p = 0;
        for(int i = 0; i < mCheckedItems.size(); i++) {
            if(mCheckedItems.valueAt(i)) {
                positions[p] = mCheckedItems.keyAt(i);
                p++;
            }
        }
        return positions;
    }

    /**
     * @return An array of IDs of all of the checked items
     */
    public long[] getCheckedItemIds() {
        long[] ids = new long[mNumCheckedItems];
        int p = 0;
        for(int i = 0; i < mCheckedItems.size(); i++) {
            if(mCheckedItems.valueAt(i)) {
                ids[p] = getItemId(mCheckedItems.keyAt(i));
                p++;
            }
        }
        return ids;
    }

    /**
     * @return The checked item states
     */
    public SparseBooleanArray getCheckedItems() {
        return mCheckedItems;
    }
}
