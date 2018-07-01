package net.wilczak.freedivecomp.android.ui.application;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.wilczak.freedivecomp.android.BR;
import net.wilczak.freedivecomp.android.R;

import java.util.ArrayList;
import java.util.List;

public class BindableList extends RecyclerView {
    private BindableAdapter adapter;
    private LinearLayoutManager layoutManager;

    public BindableList(Context context) {
        this(context, null, 0);
    }

    public BindableList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public BindableList(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BindableList, defStyle, 0);
        int defaultLayoutId = ta.getInteger(R.styleable.BindableList_templateSelector, 0);
        ta.recycle();

        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        adapter = new BindableAdapter();
        setItemAnimator(new NoChangeItemAnimator());
        setLayoutManager(layoutManager);
        setAdapter(new BindableAdapter());
        setTemplateSelector(defaultLayoutId);
    }

    public void setTemplateSelector(@LayoutRes int layoutId) {
        setTemplateSelector(new DefaultTemplateSelector(layoutId));
    }

    public void setTemplateSelector(TemplateSelector templateSelector) {
        adapter.setTemplateSelector(templateSelector);
        setAdapter(adapter);    // make recycler clear all views
    }

    public void setItems(List items) {
        adapter.setItems(items);
    }

    public interface TemplateSelector {
        int getItemType(Object viewModel);

        BindableViewHolder createViewHolder(ViewGroup parent, int itemType);
    }

    public static class NoChangeItemAnimator extends DefaultItemAnimator {
        @Override
        public boolean getSupportsChangeAnimations() {
            return false;
        }
    }

    public static class DefaultTemplateSelector implements TemplateSelector {
        private final int defaultLayoutId;
        private final List<Class> classes;
        private final List<Integer> layouts;

        public DefaultTemplateSelector(@LayoutRes int defaultLayoutId) {
            this.defaultLayoutId = defaultLayoutId;
            this.classes = new ArrayList<>();
            this.layouts = new ArrayList<>();
        }

        public DefaultTemplateSelector bind(Class viewModelClass, int layoutId) {
            int existingIndex = classes.indexOf(viewModelClass);
            if (existingIndex < 0) {
                this.classes.add(viewModelClass);
                this.layouts.add(layoutId);
            } else {
                this.layouts.set(existingIndex, layoutId);
            }
            return this;
        }

        @Override
        public int getItemType(Object viewModel) {
            if (classes.size() == 0) return 0;
            int existingIndex = classes.indexOf(viewModel.getClass());
            return existingIndex < 0 ? 0 : 1 + existingIndex;
        }

        @Override
        public BindableViewHolder createViewHolder(ViewGroup parent, int itemType) {
            int layoutId = itemType == 0 ? defaultLayoutId : layouts.get(itemType - 1);

            if (layoutId == 0) {
                return new FallbackTextViewViewHolder(parent);
            } else {
                return new BindableLayoutViewHolder(parent, layoutId);
            }
        }
    }

    public static class BindableAdapter extends Adapter<BindableViewHolder> {
        private final List items;
        private TemplateSelector templateSelector;

        public BindableAdapter() {
            this.items = new ArrayList();
        }

        @Override
        public int getItemViewType(int position) {
            return templateSelector.getItemType(items.get(position));
        }

        @NonNull
        @Override
        public BindableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return templateSelector.createViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull BindableViewHolder holder, int position) {
            holder.bind(items.get(position));
        }

        @Override
        public void onViewRecycled(@NonNull BindableViewHolder holder) {
            holder.unbind();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void setTemplateSelector(TemplateSelector templateSelector) {
            this.templateSelector = templateSelector;
        }

        @SuppressWarnings("unchecked")
        public void setItems(List items) {
            this.items.clear();
            if (items != null) {
                this.items.addAll(items);
            }
        }
    }

    public static abstract class BindableViewHolder extends ViewHolder {

        public BindableViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bind(Object viewModel);

        public void unbind() {
        }
    }

    public static class FallbackTextViewViewHolder extends BindableViewHolder {
        public FallbackTextViewViewHolder(ViewGroup parent) {
            super(new TextView(parent.getContext()));
        }

        @Override
        public void bind(Object viewModel) {
            TextView textView = (TextView) itemView;
            String value = viewModel.toString();
            textView.setText(value);
        }
    }

    public static class BindableLayoutViewHolder extends BindableViewHolder {
        private ViewDataBinding binding;

        public BindableLayoutViewHolder(ViewGroup parent, int layoutId) {
            super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(Object viewModel) {
            binding.setVariable(BR.viewModel, viewModel);
        }

        public void unbind() {
            binding.unbind();
        }
    }
}
