package org.nearbyshops.enduser.zzSavedCode;

/**
 * Created by sumeet on 22/6/16.
 */
public class CurrencyCode {



    /*

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void currencyCodes()
    {

        String[] countries = Locale.getISOLanguages();


        Locale locale =  new Locale("en","IN");

        Locale.Builder builder = new Locale.Builder();

        //builder.addUnicodeLocaleAttribute("abcd");



        Locale[] locales = Locale.getAvailableLocales();

        Currency currency = Currency.getInstance(locale);


        String currencySymbols = "";

        for(Object curr: Currency.getAvailableCurrencies().toArray())
        {
            Currency cur = (Currency)curr;

            currencySymbols = currencySymbols + cur.getSymbol() + "\n";
        }


        TextView currencyCode = (TextView) findViewById();


        String countryString = "";

        for(String country: countries)
        {
            countryString = countryString + country + " ";
        }

        currencyCode.setText(currency.getSymbol() + " " + currency.getCurrencyCode() + " " + currency.getDisplayName()
                                        + "\n"  + locale.getDisplayCountry()
                                        + "\n"  + locale.getCountry()
                                        + "\n"  + locale.getISO3Country()
                                        + "\n"  + getResources().getString(R.string.Rs)
        );


        currencyCode.setText(currencySymbols);


    }

    */

}
