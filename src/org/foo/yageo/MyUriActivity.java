package org.foo.yageo;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

public class MyUriActivity extends Activity {

	@Override
	protected void onStart() {
		String sLat, sLon;
		double lat, lon;

		super.onStart();
		Intent intent = getIntent();
		if (intent == null) return;
		Uri uri = intent.getData();
		if (uri == null) return;
		if ("geo".equals(uri.getScheme())) {
			String s = uri.getSchemeSpecificPart();
			if (s == null) return;
			s = s.split(";")[0];
			String[] a = s.split(",");
			if (a.length <2) return;
			sLat = a[0];
			sLon = a[1];
		} else if ("www.foo.org".equals(uri.getHost())) {
			sLat = uri.getQueryParameter("lat");
			sLon = uri.getQueryParameter("lon");
		} else {
			return;
		}
		lat = Double.parseDouble(sLat);
		lon = Double.parseDouble(sLon);

		// Создаем интент для построения маршрута
		intent = new Intent("ru.yandex.yandexnavi.action.BUILD_ROUTE_ON_MAP");
		intent.setPackage("ru.yandex.yandexnavi");

		PackageManager pm = getPackageManager();
		List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);

		// Проверяем, установлен ли Яндекс.Навигатор
		if (infos == null || infos.size() == 0) {
			// Если нет - будем открывать страничку Навигатора в Google Play
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=ru.yandex.yandexnavi"));
		} else {
			intent.putExtra("lat_to", lat);
			intent.putExtra("lon_to", lon);
		}

		// Запускаем нужную Activity
		startActivity(intent);
		this.finish();
	}
}
