package com.example.travel01.ui.personalinfo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.travel01.R;

public class PersonalViewModel extends AndroidViewModel {
    SavedStateHandle handle;
    public String username = getApplication().getResources().getString(R.string.username);
    public String shpname = getApplication().getResources().getString(R.string.shpname);

    public PersonalViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        this.handle = handle;

        if (!handle.contains(username)) {
            load();
        }
    }

    public LiveData<String> getusername() {
        return handle.getLiveData(username);
    }

    void load() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(shpname, Context.MODE_PRIVATE);
        String str = sharedPreferences.getString(username, "");
        handle.set(username, str);

    }

    void save() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(shpname, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(username, getusername().getValue());
        editor.apply();
    }

    void  setUsername(String name){
        handle.set(username,name);
    }

}
