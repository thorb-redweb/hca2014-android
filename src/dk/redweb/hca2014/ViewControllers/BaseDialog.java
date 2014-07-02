package dk.redweb.hca2014.ViewControllers;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.RedEventApplication;

/**
 * package: dk.redweb.hca2014.ViewControllers
 * copyright: Copyright (C) 2005 - 2014 redweb ApS. All rights reserved.
 * license: GNU General Public License version 2 or later.
 */
public abstract class BaseDialog {
    protected FragmentActivity _activity;
    protected RedEventApplication _app;
    protected Dialog _dialog;
    protected BasePageFragment _fragment;

    private FrameLayout _frmPadding;
    public BasePageFragment getFragment()
    {
        return _fragment;
    }
    public FragmentActivity getActivity()
    {
        return _activity;
    }

    public BaseDialog(BasePageFragment fragment, int layoutId)
    {
        _activity = fragment.getActivity();
        _app = ((RedEventApplication)_activity.getApplication());
        _fragment = fragment;
        createDialog(layoutId);
    }

    private void createDialog(int layoutId)
    {
        _dialog = new Dialog(_activity);
        _frmPadding = new FrameLayout(_activity);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        _frmPadding.setLayoutParams(params);
        _frmPadding.setPadding(16, 16, 16, 16);
        FrameLayout frmBorder = new FrameLayout(_activity);
        frmBorder.setLayoutParams(params);
        frmBorder.setPadding(2, 2, 2, 2);
        frmBorder.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        frmBorder.addView(_activity.getLayoutInflater().inflate(layoutId, null));
        _frmPadding.addView(frmBorder);
        _dialog.setContentView(_frmPadding);
        _dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        _dialog.setCanceledOnTouchOutside(true);

        //Remove blue line divider
        int divierId = _dialog.getContext().getResources()
                .getIdentifier("android:id/titleDivider", null, null);
        View divider = _dialog.findViewById(divierId);
        divider.setBackgroundColor(getActivity().getResources().getColor(R.color.transparent));

        _dialog.show();

    }


    public void close()
    {
        _dialog.dismiss();
    }

    protected View findViewById(int id)
    {
        return _frmPadding.findViewById(id);
    }

    protected abstract void getControls();
    protected abstract void setContent();
    protected abstract void setAppearance();
    protected abstract void setupControls();
}
