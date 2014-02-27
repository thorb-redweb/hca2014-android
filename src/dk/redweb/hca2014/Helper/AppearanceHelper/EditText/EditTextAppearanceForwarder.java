package dk.redweb.hca2014.Helper.AppearanceHelper.EditText;

import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelperGetter;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/16/14
 * Time: 9:33
 */
public class EditTextAppearanceForwarder {
    private AppearanceHelper _helper;
    private AppearanceHelperGetter _getter;
    private EditTextAppearanceHelper _flxBtnHelper;

    public EditTextAppearanceForwarder(AppearanceHelper helper, AppearanceHelperGetter getter){
        _helper = helper;
        _getter = getter;
        _flxBtnHelper = new EditTextAppearanceHelper(helper, getter);
    }
}
