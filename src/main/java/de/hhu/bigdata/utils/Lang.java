package de.hhu.bigdata.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains a Mapping betweetn BCP 47 code and {@link Country}
 */
public class Lang {
	private static Map<String, Country> countryHashMap = new HashMap<>();

	static {
		countryHashMap.put("am", new Country(9.145F, 40.489673F, "Ethiopia-Amharic"));
		countryHashMap.put("ar", new Country(26.3351F, 17.228331F, "Libya-Arabic"));
		countryHashMap.put("hy", new Country(40.069099F, 45.038189F, "Armenia"));
		countryHashMap.put("bn", new Country(23.684994F, 90.356331F, "Bangladesh-Bengali"));
		countryHashMap.put("bg", new Country(42.733883F, 25.48583F, "Bulgaria"));
		countryHashMap.put("my", new Country(21.913965F, 95.956223F, "Myanmar[Burma]-Burmese"));
		countryHashMap.put("zh", new Country(35.86166F, 104.195397F, "China"));
		countryHashMap.put("cs", new Country(49.817492F, 15.472962F, "Czech Republic"));
		countryHashMap.put("da", new Country(56.26392F, 9.501785F, "Denmark"));
		countryHashMap.put("nl", new Country(52.132633F, 5.291266F, "Netherlands-Dutch"));
		countryHashMap.put("en", new Country(37.09024F, -95.712891F, "United States-English"));
		countryHashMap.put("et", new Country(58.595272F, 25.013607F, "Estonia"));
		countryHashMap.put("fi", new Country(61.92411F, 25.748151F, "Finland"));
		countryHashMap.put("fr", new Country(46.227638F, 2.213749F, "France"));
		countryHashMap.put("ka", new Country(42.315407F, 43.356892F, "Georgia"));
		countryHashMap.put("de", new Country(51.165691F, 10.451526F, "Germany"));
		countryHashMap.put("el", new Country(39.074208F, 21.824312F, "Greece"));
		countryHashMap.put("gu", new Country(22.908311F, 71.124942F, "Gujarat"));
		countryHashMap.put("ht", new Country(18.971187F, -72.285215F, "Haiti"));
		countryHashMap.put("iw", new Country(31.046051F, 34.851612F, "Israel-Hebrew"));
		countryHashMap.put("hi", new Country(20.593684F, 78.96288F, "India-Hindi"));
		countryHashMap.put("hu", new Country(47.162494F, 19.503304F, "Hungary"));
		countryHashMap.put("is", new Country(64.963051F, -19.020835F, "Iceland"));
		countryHashMap.put("in", new Country(-0.789275F, 113.921327F, "Indonesia"));
		countryHashMap.put("it", new Country(41.87194F, 12.56738F, "Italy"));
		countryHashMap.put("ja", new Country(36.204824F, 138.252924F, "Japan"));
		countryHashMap.put("kn", new Country(14.493956F, 75.696953F, "Kanataka-Kannada"));
		countryHashMap.put("km", new Country(12.565679F, 104.990963F, "Cambodia-Khmer"));
		countryHashMap.put("ko", new Country(35.907757F, 127.766922F, "South Korea"));
		countryHashMap.put("lo", new Country(19.85627F, 102.495496F, "Laos"));
		countryHashMap.put("lv", new Country(56.879635F, 24.603189F, "Latvia"));
		countryHashMap.put("lt", new Country(55.169438F, 23.881275F, "Lithuania"));
		countryHashMap.put("ml", new Country(4.210484F, 101.975766F, "Malaysia-Malayalam"));
		countryHashMap.put("dv", new Country(3.202778F, 73.22068F, "Maldives"));
		countryHashMap.put("mr", new Country(19.371544F, 75.523669F, "Maharashtra-Marathi"));
		countryHashMap.put("ne", new Country(28.394857F, 84.124008F, "Nepal"));
		countryHashMap.put("no", new Country(60.472024F, 8.468946F, "Norway"));
		countryHashMap.put("or", new Country(20.235883F, 84.348490F, "Odisha-Oriya"));
		countryHashMap.put("pa", new Country(30.805161F, 75.497555F, "Punjab-Panjabi"));
		countryHashMap.put("ps", new Country(33.93911F, 67.709953F, "Afghanistan-Pashto"));
		countryHashMap.put("fa", new Country(32.427908F, 53.688046F, "Iran-Persian"));
		countryHashMap.put("pl", new Country(51.919438F, 19.145136F, "Poland"));
		countryHashMap.put("pt", new Country(-14.235004F, -51.92528F, "Brazil-Portuguese"));
		countryHashMap.put("ro", new Country(45.943161F, 24.96676F, "Romania"));
		countryHashMap.put("ru", new Country(61.52401F, 105.318756F, "Russia"));
		countryHashMap.put("sr", new Country(44.016521F, 21.005859F, "Serbia"));
		countryHashMap.put("sd", new Country(25.935829F, 68.558585F, "Sindh-Sindhi"));
		countryHashMap.put("si", new Country(7.873054F, 80.771797F, "Sri Lanka-Sinhala"));
		countryHashMap.put("sk", new Country(48.669026F, 19.699024F, "Slovakia"));
		countryHashMap.put("sl", new Country(46.151241F, 14.995463F, "Slovenia"));
		countryHashMap.put("ckb", new Country(33.223191F, 43.679291F, "Iraq-Kurdish"));
		countryHashMap.put("es", new Country(40.463667F, -3.74922F, "Spain"));
		countryHashMap.put("sv", new Country(60.128161F, 18.643501F, "Sweden"));
		countryHashMap.put("tl", new Country(12.879721F, 121.774017F, "Philippines-Tagalog"));
		countryHashMap.put("ta", new Country(10.449872F, 78.715992F, "Tamil Nadu-Tamil"));
		countryHashMap.put("te", new Country(15.241323F, 78.861580F, "Andhra Pradesh-Telugu"));
		countryHashMap.put("th", new Country(15.870032F, 100.992541F, "Thailand-Thai"));
		countryHashMap.put("bo", new Country(31.807145F, 89.391520F, "Tibet-Tibetan"));
		countryHashMap.put("tr", new Country(38.963745F, 35.243322F, "Turkey-Turkish"));
		countryHashMap.put("uk", new Country(48.379433F, 31.16558F, "Ukraine"));
		countryHashMap.put("ur", new Country(30.375321F, 69.345116F, "Pakistan-Urdu"));
		countryHashMap.put("ug", new Country(39.790308F, 85.385459F, "Xinjiang-Uyghur"));
		countryHashMap.put("vi", new Country(14.058324F, 108.277199F, "Vietnam"));
		countryHashMap.put("cy", new Country(52.290373F, -3.797590F, "Wales-Welsh"));
	}

	/**
	 * Get the latitude for the given BCP 47 Lang code
	 *
	 * @param lang BCP 47 code
	 * @return latitude or -80F
	 */
	public static Float langToLat(String lang) {
		Country c = countryHashMap.get(lang.toLowerCase().replaceAll("([a-z]+)\\-.*", "$1"));
		return c != null ? c.lat : -80F;
	}

	/**
	 * Get the longitude for the given BCP 47 Lang code
	 *
	 * @param lang BCP 47 code
	 * @return longitude or -80F
	 */
	public static Float langToLon(String lang) {
		Country c = countryHashMap.get(lang.toLowerCase());
		return c != null ? c.lon : 80F;
	}
}
