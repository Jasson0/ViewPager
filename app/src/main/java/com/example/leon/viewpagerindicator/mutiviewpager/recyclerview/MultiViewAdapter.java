package com.example.leon.viewpagerindicator.mutiviewpager.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leon.viewpagerindicator.R;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.holder.BaseViewHolder;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.model.VisitableModel;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.type.TypeFactory;
import com.example.leon.viewpagerindicator.mutiviewpager.recyclerview.type.TypeFactoryForList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 2017/9/14.
 * ●　　抽象访问者(Visitor)角色：声明了一个或者多个方法操作，形成所有的具体访问者角色必须实现的接口。（对应typeFactory）
 * <p>
 * 　●　　具体访问者(ConcreteVisitor)角色：实现抽象访问者所声明的接口，也就是抽象访问者所声明的各个访问操作。（对应typeFactoryList）
 * <p>
 * 　●　　抽象节点(Node)角色：声明一个接受操作，接受一个访问者对象作为一个参数。(对应visitableModel)
 * <p>
 * 　●　　具体节点(ConcreteNode)角色：实现了抽象节点所规定的接受操作。（对应其他实现了对应visitableModel的model）
 * <p>
 * 　●　　结构对象(ObjectStructure)角色：有如下的责任，可以遍历结构中的所有元素；
 * 如果需要，提供一个高层次的接口让访问者对象可以访问每一个元素；如果需要，可以设计成一个复合对象或者一个聚集，
 * 如List或Set。（holder中的类）
 */

public class MultiViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    @Override
    public int getItemViewType(int position) {
        return models.get(position).type(typeFactory);
    }

    private List<VisitableModel> models;
    private TypeFactory typeFactory;
    private Context context;
    public BaseViewHolder viewHolder;
    public Test test;

    public void setTest(Test test) {
        this.test = test;
    }

    public interface Test {
        void t();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(viewType, parent,
                false);
        viewHolder = typeFactory.createViewHolder(viewType, itemView, this);
        return typeFactory.createViewHolder(viewType, itemView, this);
    }

    public void fuck() {
        if (test != null) {
            test.t();
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setUpView(models.get(position), position, this);
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
//        super.onViewAttachedToWindow(holder);
        Log.e("leon", "000000");
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
        Log.e("leon", "11111111");
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
//        super.registerAdapterDataObserver(observer);
        Log.e("leon", "22222222");
    }

    public List<VisitableModel> getDataList() {
        if (models == null)
            models = new ArrayList<>();
        return models;
    }

    public void setDataList(List<VisitableModel> dataList) {
        this.models = dataList;
    }

    public MultiViewAdapter(Context context, List<VisitableModel> dataList) {
        this.models = dataList;
        this.context = context;
        /**
         * 通过接口抽象，将所有与列表相关的Model抽象为Visitable，当我们在初始化数据源时就能以List<Visitable>的形式将不同类型的Model集合在列表中；
         *通过访问者模式，将列表类型判断的相关代码抽取到TypeFactoryForList 中，同时所有列表类型对应的布局资源都在这个类中进行管理与维护，以这样的方式巧妙的增强了扩展性与可维护性；
         *getItemViewType中不再需要进行if判断，通过数据源控制列表的布局类型，同时返回的不再是简单的布局类型标识，而是布局的资源ID（通过modelList.get(position).type()获取），
         *进一步简化代码（在onCreateViewHolder中会体现出来）；
         */
        this.typeFactory = new TypeFactoryForList();
    }


    @Override
    public int getItemCount() {
        return models.size();
    }
}
