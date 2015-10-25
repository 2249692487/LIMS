package cn.edu.bzu.lims.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.List;

import cn.edu.bzu.lims.R;
import cn.edu.bzu.lims.bean.News;
import cn.edu.bzu.lims.util.ImageLoader;

/**
 * Created by monster on 2015/10/21.
 */
public class InformRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private LayoutInflater mInflate;
    private List<News> mList;
    private Context mContext;

    /**
     * 声明一个接口，用于实现点击事件
     */
    public interface  OnItemClickListener{
        void OnItemClick(int position,View view);
        void OnItemLongClick(int position,View view);
    }

    private OnItemClickListener mOnItemClickListener;

    public  void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener=listener;
    }


    public InformRecyclerAdapter(List<News> list,Context context){
        this.mList=list;
        this.mContext=context;
        mInflate=LayoutInflater.from(mContext);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view=mInflate.inflate(R.layout.activity_rv_item,parent,false);
//        MyViewHolder myViewHolder=new MyViewHolder(view);
//        return myViewHolder;

        //  含有水滴反馈的效果
        return new MyViewHolder(
                MaterialRippleLayout.on(mInflate.inflate(R.layout.activity_rv_item, parent, false))
                        .rippleOverlay(true)  //如果这是真的,涟漪在前景;错:背景
                        .rippleAlpha(0.2f)   //α的涟漪
                        .rippleColor(0xFF585858)
                        .rippleHover(true)  /// /如果这是真的,一个悬停效果是当视图是感动
                        .create()
        );
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        String url=mList.get(position).getUser().getUserPhoto().getFileUrl(mContext);
        //String url="http://file.bmob.cn/M02/35/15/oYYBAFYoWBiAJvSCAACm3U5ozvw961.jpg";
        holder.iv_userPhoto.setTag(url);
        new ImageLoader().showImageByAsyncTask(holder.iv_userPhoto,url);
        holder.tv_name.setText(mList.get(position).getUser().getName());
        holder.tv_content.setText(mList.get(position).getContent());
        holder.tv_date.setText(mList.get(position).getUpdatedAt());
        holder.tv_objectId.setText(mList.get(position).getObjectId());

        if (mOnItemClickListener!=null){

            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int LayoutPosition=holder.getLayoutPosition(); //得到布局的position
                    mOnItemClickListener.OnItemClick(LayoutPosition,holder.itemView);

                }
            });

            //longclickListener
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int LayoutPosition=holder.getLayoutPosition(); //得到布局的position
                    mOnItemClickListener.OnItemLongClick(LayoutPosition,holder.itemView);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView iv_userPhoto;
    TextView tv_name,tv_date,tv_content,tv_objectId;
    public MyViewHolder(View itemView) {
        super(itemView);
        tv_objectId= (TextView) itemView.findViewById(R.id.tv_objectId);
        iv_userPhoto= (ImageView) itemView.findViewById(R.id.iv_userPhoto);
        tv_name= (TextView) itemView.findViewById(R.id.tv_name);
        tv_date= (TextView) itemView.findViewById(R.id.tv_date);
        tv_content= (TextView) itemView.findViewById(R.id.tv_content);
    }
}