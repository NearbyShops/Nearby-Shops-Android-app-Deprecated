package org.nearbyshops.core.DaggerComponents;


import org.nearbyshops.core.DaggerModules.AppModule;
import org.nearbyshops.core.DaggerModules.NetModule;
import org.nearbyshops.core.SignUp.ForgotPassword.FragmentCheckResetCode;
import org.nearbyshops.core.SignUp.ForgotPassword.FragmentEnterCredentials;
import org.nearbyshops.core.SignUp.ForgotPassword.FragmentResetPassword;
import org.nearbyshops.core.SignUp.FragmentEmailOrPhone;
import org.nearbyshops.core.SignUp.FragmentEnterPassword;
import org.nearbyshops.core.SignUp.FragmentVerify;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sumeet on 14/5/16.
 */

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {


    void Inject(FragmentEmailOrPhone fragmentEmailOrPhone);

    void Inject(FragmentVerify fragmentVerify);

    void Inject(FragmentEnterPassword fragmentEnterPassword);

    void Inject(FragmentEnterCredentials fragmentEnterCredentials);

    void Inject(FragmentCheckResetCode fragmentCheckResetCode);

    void Inject(FragmentResetPassword fragmentResetPassword);


}
