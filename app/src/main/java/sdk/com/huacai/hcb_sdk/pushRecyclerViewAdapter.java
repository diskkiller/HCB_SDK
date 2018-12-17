package sdk.com.huacai.hcb_sdk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;



/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class pushRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<JData> modelList;

    private OnItemClickListener mItemClickListener;


    public pushRecyclerViewAdapter(Context context, ArrayList<JData> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<JData> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.left_item_recycler_list2, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final JData model = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.itemTxtMessage.setText(model.desc);

        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private JData getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, JData model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTxtTitle;
        private TextView itemTxtMessage;


        // @BindView(R.id.img_user)
        // ImageView imgUser;
        // @BindView(R.id.item_txt_title)
        // TextView itemTxtTitle;
        // @BindView(R.id.item_txt_message)
        // TextView itemTxtMessage;
        // @BindView(R.id.radio_list)
        // RadioButton itemTxtMessage;
        // @BindView(R.id.check_list)
        // CheckBox itemCheckList;
        public ViewHolder(final View itemView) {
            super(itemView);

            this.itemTxtMessage = (TextView) itemView.findViewById(R.id.item_txt_message);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mItemClickListener != null)
                        mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));

                }
            });

        }
    }

}

