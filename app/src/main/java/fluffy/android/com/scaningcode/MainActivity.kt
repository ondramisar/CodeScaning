package fluffy.android.com.scaningcode

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val CAMERA_PERMISSION = 100
    private var mDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
    }

    override fun onStart() {
        super.onStart()


    }

    override fun onStop() {
        super.onStop()

        mDisposable?.dispose()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION -> {
                if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                    showCamera()
                } else {
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showCamera() {
        barcodeView.visibility = View.VISIBLE
        mDisposable = barcodeView
                .getObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { barcode ->
                            barcodeView.visibility = View.GONE
                            text.visibility = View.VISIBLE
                            text.text = barcode.rawValue
                        },
                        { throwable ->
                            //handle exceptions like no available camera for selected facing
                        })
    }

}
