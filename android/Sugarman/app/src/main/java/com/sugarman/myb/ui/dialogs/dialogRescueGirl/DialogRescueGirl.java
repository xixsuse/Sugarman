package com.sugarman.myb.ui.dialogs.dialogRescueGirl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arellomobile.mvp.MvpDialogFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.CustomPicasso;
import com.sugarman.myb.R;
import com.sugarman.myb.api.models.responses.Member;
import com.sugarman.myb.api.models.responses.Tracking;
import com.sugarman.myb.ui.fragments.rescue_challenge.adapters.RescueMembersAdapter;
import com.sugarman.myb.ui.views.CropSquareTransformation;
import com.sugarman.myb.ui.views.MaskTransformation;
import com.sugarman.myb.utils.VibrationHelper;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

/**
 * Created by nikita on 11.12.2017.
 */

public class DialogRescueGirl extends MvpDialogFragment implements IDialogRescueGirlView {
  private static final String DIALOG_RESCUE_GIRL_KEY = "DIALOG_RESCUE_GIRL_KEY";
  @InjectPresenter DialogRescueGirlPresenter mPresenter;
  @BindView(R.id.group_avatar) ImageView mImageViewGroupAvatar;
  @BindView(R.id.ivCross) ImageView mImageViewCross;
  @BindView(R.id.tvFailText) TextView mTextViewFailText;
  @BindView(R.id.rvFailures) RecyclerView mRecyclerViewFailures;
  @BindView(R.id.tvNumToRescue) TextView mTextViewNumToRescue;
  @BindView(R.id.tvYouGroupRescued) TextView mTextViewYouGotXRescues;
  @BindView(R.id.ivKick) ImageView mImageViewKick;
  @BindView(R.id.tvKickcThemNow) TextView mTextViewKickThemNow;
  private Tracking mTracking;
  private int failuresCount = 0;
  private List<Member> failures = new ArrayList<>();
  private RescueMembersAdapter mRescueMembersAdapter;
  private int saverCount;

  public static DialogRescueGirl newInstance(Tracking tracking) {
    Bundle args = new Bundle();
    args.putParcelable(DIALOG_RESCUE_GIRL_KEY, tracking);
    DialogRescueGirl fragment = new DialogRescueGirl();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mTracking = getArguments().getParcelable(DIALOG_RESCUE_GIRL_KEY);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.dialog_rescue_girl, container, false);
    ButterKnife.bind(this, rootView);
    setUpUi();
    return rootView;
  }

  private void setUpUi() {
    CustomPicasso.with(mImageViewGroupAvatar.getContext())
        .load(mTracking.getGroup().getPictureUrl())
        .transform(new CropSquareTransformation())
        .transform(
            new MaskTransformation(mImageViewGroupAvatar.getContext(), R.drawable.profile_mask,
                false, 0xfff))
        .into(mImageViewGroupAvatar);

    mTextViewFailText.setText(String.format(getString(R.string.your_group_has_failed_thanks_to),
        mTracking.getGroup().getName()));

    for (int i = 0; i < mTracking.getMembers().length; i++) {
      if (mTracking.getMembers()[i].getFailureStatus() == Member.FAIL_STATUS_FAILUER) {
        failuresCount++;
      }
    }
    mTextViewNumToRescue.setText(
        String.format(getString(R.string.text_num_to_rescue), (int) failuresCount));

    mRecyclerViewFailures.setLayoutManager(
        new LinearLayoutManager(mRecyclerViewFailures.getContext(), LinearLayoutManager.HORIZONTAL,
            false));
    for (Member m : mTracking.getMembers()) {
      Timber.e("status " + m.getFailureStatus());
      if (m.getFailureStatus() == Member.FAIL_STATUS_FAILUER
          || m.getFailureStatus() == Member.FAIL_STATUS_SAVED) {
        failures.add(m);
      }
    }
    mRescueMembersAdapter = new RescueMembersAdapter(getMvpDelegate());
    //mRescueMembersAdapter.setMembers(Arrays.asList(mTracking.getMembers()));
    mRescueMembersAdapter.setMembers(failures);
    mRecyclerViewFailures.setAdapter(mRescueMembersAdapter);

    for (Member m : mTracking.getMembers()) {
      if (m.getFailureStatus() == Member.FAIL_STATUS_SAVED) {
        saverCount++;
      }
    }
    
    mTextViewYouGotXRescues.setText(
        String.format(getString(R.string.you_got_x_rescues), (int) saverCount));
    
  }

  @OnClick(R.id.ivCross) public void ivCrossClick() {
    getDialog().cancel();
  }

  @OnClick({ R.id.ivKick, R.id.tvKickcThemNow }) public void kickAllFailures() {
    YoYo.with(Techniques.Shake).duration(700).playOn(mTextViewKickThemNow);
    Timber.e("kickAllFailures " + failures.size());
    mPresenter.superPoke(failures, mTracking.getId());
  }

  @Override public void superKickResponse() {
    VibrationHelper.simpleVibration(getActivity(), 1300L);
  }
}
