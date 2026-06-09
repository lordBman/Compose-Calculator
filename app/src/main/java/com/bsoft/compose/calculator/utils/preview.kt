package com.bsoft.compose.calculator.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview


@Preview(showBackground = false, name = "Light Mode Preview", device = "id:pixel_4", apiLevel = 33)
@Preview(showBackground = false, name = "Dark Mode Preview ", uiMode = Configuration.UI_MODE_NIGHT_YES, device = "id:pixel_4", apiLevel = 33)
@Preview(showBackground = false, name = "Light Mode Preview",
    device = "spec:parent=Nexus 10,orientation=portrait", apiLevel = 33,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_DESK
)
@Preview(showBackground = false, name = "Light Mode Preview",
    device = "spec:parent=Nexus 10,orientation=landscape", apiLevel = 33,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_DESK
)
annotation class UIPreview;