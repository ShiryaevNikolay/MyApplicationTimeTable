package com.example.timetable.modules;

import android.content.Context;
import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable.R;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private ItemTouchHelperAdapter mAdapter;
    private Context context;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter, Context context) {
        this.mAdapter = adapter;
        this.context = context;
    }

    public boolean isLongPressDragEnabled() {
        return false;
    }

    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    // какие направления для перетаскивания будут поддерживаться
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addBackgroundColor(ContextCompat.getColor(context, R.color.colorCancel))
                .addActionIcon(R.drawable.ic_delete)
                .addSwipeLeftLabel("Удалить")
                .addSwipeRightLabel("Удалить")
                .setSwipeLeftLabelColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSwipeRightLabelColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
