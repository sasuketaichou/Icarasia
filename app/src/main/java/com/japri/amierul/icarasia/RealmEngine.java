package com.japri.amierul.icarasia;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;

public class RealmEngine {

    private Context context;
    private Realm realm = null;
    private RealmConfiguration realmConfig;

    public RealmEngine(Context context) {
        this.context = context;
    }

    private RealmConfiguration getRealmConfig(){
        if (realmConfig == null)
            Realm.init(context);
        realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();

        return realmConfig;
    }

    public Realm getRealm(){
        RealmConfiguration config = getRealmConfig();
        return realm == null ? Realm.getInstance(config):realm;
    }

    public UserModel checkIfExist(String email) {

        Realm realm = getRealm();

        RealmQuery<UserModel> query = realm.where(UserModel.class);
        UserModel existModel = query.equalTo("email",email).findFirst();

        return existModel ;
    }

    public void createUserAcc (final UserModel form, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError) {

        Realm dRealm = getRealm();

        dRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                UserModel userModel = realm.createObject(UserModel.class,form.getEmail());
                userModel.setPassword(form.getPassword());
                userModel.setMobile(form.getMobile());
                userModel.setUserType(form.getUserType());
                userModel.setFirstName(form.getFirstName());
                userModel.setLastName(form.getLastName());

            }
        }, onSuccess,onError);
    }

    public void setMobile(final String email, final String mobile, Realm.Transaction.OnSuccess onSuccess , Realm.Transaction.OnError onError) {

        Realm realm = getRealm();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                UserModel userModel = checkIfExist(email);
                userModel.setMobile(mobile);
            }
        }, onSuccess, onError);
    }
}
