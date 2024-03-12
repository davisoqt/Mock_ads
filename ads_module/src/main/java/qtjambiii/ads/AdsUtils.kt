package qtjambiii.ads

import android.app.Application
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import com.blankj.utilcode.util.LogUtils
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import java.util.concurrent.atomic.AtomicReference

class AdsUtils {

    companion object {
        //app reference
        val app = AtomicReference<Application>()

        //interistial admob
        lateinit var interstitialAd: InterstitialAd
        lateinit var mRewardedAd: RewardedAd

        lateinit var reviewManager: ReviewManager
        lateinit var reviewInfo: ReviewInfo

        //action to perform on adclosed
        lateinit var onClose: () -> Unit

        lateinit var adsManager: DashManager
        val adsScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        var isRewardEarned: Boolean = false

        fun initManager(app: Application) {
            Companion.app.set(app)
            adsManager = DashManager(app)
            //load admob
            loadInteristial()
            loadRewarded()

            reviewManager = ReviewManagerFactory.create(app)
            reviewManager.requestReviewFlow().addOnCompleteListener {
                if (it.isSuccessful) reviewInfo = it.result
            }
        }

        private fun loadInteristial() {
            InterstitialAd.load(
                app.get(),
                app.get().getString(R.string.interistial),
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        LogUtils.d(p0.message)
                        super.onAdFailedToLoad(p0)
                    }

                    override fun onAdLoaded(p0: InterstitialAd) {
                        LogUtils.d("Interistial loaded")
                        interstitialAd = p0
                        interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                onClose()
                                loadInteristial()
                            }
                        }
                    }
                });
        }

        private fun loadRewarded() {
            var adRequest = AdRequest.Builder().build()
            RewardedAd.load(app.get(), app.get().getString(R.string.rewarded), adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd
                }
            })
        }

        fun showRewarded(activity: ComponentActivity) {
            if (::mRewardedAd.isInitialized) {
                mRewardedAd.show(activity) {
                    isRewardEarned = true
                }
            }
        }


        fun showInterstitial(mActivity: ComponentActivity, action_close: () -> Unit) {
            if (!::interstitialAd.isInitialized) {
                action_close()
                return
            }
            onClose = action_close

            adsScope.launch {
                if (adsManager.canShowInteristial() && adsManager.getIsEnabled() == 1 && !isRewardEarned) {
                    adsManager.setLastTime(DateTime().getMillis())
                    withContext(Dispatchers.Main) {
                        interstitialAd.show(mActivity)
                    }
                } else withContext(Dispatchers.Main) { onClose() }

            }
        }

        fun showRateFlow(activity: ComponentActivity, action: () -> Unit) {
            adsScope.launch(Dispatchers.IO) {
                if (::reviewInfo.isInitialized && !adsManager.isAppRated() && adsManager.getClickHistory() % 3 == 0)
                    withContext(Dispatchers.Main) {
                        reviewManager.launchReviewFlow(activity, reviewInfo).addOnCompleteListener {
                            action()
                        }
                    }
                else withContext(Dispatchers.Main) { action() }
            }
        }

        fun loadBannerAdaptativeBanner(activity: ComponentActivity, container: ViewGroup) {
            val adView = AdView(activity)
            container.addView(adView)
            adView.adUnitId = activity.getString(R.string.banner)
            adView.setAdSize(adSize(activity, container))
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }

        private fun adSize(activity: ComponentActivity, adViewContainer: View): AdSize {
            val display = activity.windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = adViewContainer.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
        }
    }
}