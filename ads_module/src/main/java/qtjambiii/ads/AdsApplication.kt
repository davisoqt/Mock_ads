package qtjambiii.ads

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.onesignal.OneSignal
import net.danlew.android.joda.JodaTimeAndroid

open class AdsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this)
        JodaTimeAndroid.init(this)
        try {
            MobileAds.initialize(this)
            OneSignal.initWithContext(this, getString(R.string.onesignal_id))
            AdsUtils.initManager(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}