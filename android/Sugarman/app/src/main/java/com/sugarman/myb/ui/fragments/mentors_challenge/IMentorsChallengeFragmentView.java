package com.sugarman.myb.ui.fragments.mentors_challenge;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by nikita on 26.10.2017.
 */
@StateStrategyType(AddToEndSingleStrategy.class) public interface IMentorsChallengeFragmentView
    extends MvpView {
  void setUnreadMessages(int size);
}
