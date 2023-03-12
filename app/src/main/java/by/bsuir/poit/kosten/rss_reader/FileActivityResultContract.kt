package by.bsuir.poit.kosten.rss_reader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.activity.result.contract.ActivityResultContract

class FileActivityResultContract: ActivityResultContract<String?, Uri?>() {
    override fun createIntent(context: Context, input: String?): Intent {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"

            putExtra(DocumentsContract.EXTRA_INITIAL_URI, input)
        }
        return intent;
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if(resultCode != Activity.RESULT_OK){
            return null
        }

        return intent?.data
    }
}