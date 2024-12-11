package com.sigit.mechaban.dashboard.montir.booking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SharedBookingViewModel extends ViewModel {
    private final MutableLiveData<List<ActivityOnbookingAdapter.ActivityOnbookingItem>> bookingsLiveData = new MutableLiveData<>();

    public LiveData<List<ActivityOnbookingAdapter.ActivityOnbookingItem>> getBookings() {
        return bookingsLiveData;
    }

    public void updateBookingList(List<ActivityOnbookingAdapter.ActivityOnbookingItem> updatedList) {
        bookingsLiveData.setValue(updatedList);
    }
}
