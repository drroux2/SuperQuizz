package fr.diginamic.formation.super_quizz.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.diginamic.formation.super_quizz.R;
import fr.diginamic.formation.super_quizz.model.Question;
import fr.diginamic.formation.super_quizz.ui.fragments.QuestionListFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Question} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 *
 */
public class QuestionRecyclerViewAdapter extends RecyclerView.Adapter<QuestionRecyclerViewAdapter.ViewHolder> {

    private  List<Question> mQuestions;
    private final OnListFragmentInteractionListener mListener;

    public QuestionRecyclerViewAdapter(List<Question> questions, OnListFragmentInteractionListener listener) {
        mQuestions = questions;
        mListener = listener;

    }
    public void updateData(List<Question> questions)     {
        this.mQuestions = questions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mQuestions.get(position);
        Uri uri = Uri.parse(mQuestions.get(position).getImageURL());
        try {
            Picasso.with(holder.context).load(uri).resize(200,200).centerCrop().into(holder.mImageViewUser);
        } catch (Exception e){
            Log.d("EXCEPTION", e.getMessage());
        }
        holder.mIdView.setText(mQuestions.get(position).getIdQuestion() + " " + mQuestions.get(position).getNameQuestion());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.startQuizzWithQuestion(holder.mItem);
                }
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.editQuestion(holder.mItem);

                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final ImageView mImageViewUser;
        private final TextView mIdView;
        private final Context context;
        private Question mItem;

         ViewHolder(View view) {
            super(view);
            mView = view;
            mImageViewUser = view.findViewById(R.id.imageview_image_user);
            context = view.getContext();
            mIdView = view.findViewById(R.id.item_number);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString();
        }
    }
}
