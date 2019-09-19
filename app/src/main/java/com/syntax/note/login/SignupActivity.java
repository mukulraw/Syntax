package com.syntax.note.login;

import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.syntax.note.HomeActivity2;
import com.syntax.note.R;
import com.syntax.note.home.HomeActivity;
import com.syntax.note.signupRequestPOJO.Data;
import com.syntax.note.signupRequestPOJO.signupRequestBean;
import com.syntax.note.signupResponsePOJO.signupResponseBean;
import com.syntax.note.utility.Constant;
import com.syntax.note.utility.SharePreferenceUtils;
import com.syntax.note.webServices.ServiceInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {
    Retrofit retrofit;

    ServiceInterface serviceInterface;

    EditText syntaxName,syntaxEmail,syntaxPhone,syntaxPassword,syntaxRePassword;
    String syntax_name,syntax_email,syntax_phone,syntax_password,syntax_repassword;
    Button syntaxSignupBtn;
    TextView syntaxSigninNow;
    RelativeLayout rootlayout;


    boolean isValid=false;

    ProgressBar pBar;

    Spinner timezone;

    String tzone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setUpWidget();
        pBar.setVisibility(View.GONE);

        //Retrofit

         retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

         serviceInterface = retrofit.create(ServiceInterface.class);

        syntaxSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                dataValidation();
                if (isValid)
                {
                    if(syntax_password.equals(syntax_repassword)) {
                        signup();
                        pBar.setVisibility(View.VISIBLE);
                    }else
                    {
                        Toast.makeText(SignupActivity.this, "password mismatch", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        syntaxSigninNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinIntent = new Intent(SignupActivity.this,SigninActivity.class);
                startActivity(signinIntent);
                finish();
            }
        });


        final List<String> tlist = new ArrayList<>();

        tlist.add("Africa/Abidjan");
        tlist.add("Africa/Accra");
        tlist.add("Africa/Addis_Ababa");
        tlist.add("Africa/Algiers");
        tlist.add("Africa/Asmara");
        tlist.add("Africa/Asmera");
        tlist.add("Africa/Bamako");
        tlist.add("Africa/Bangui");
        tlist.add("Africa/Banjul");
        tlist.add("Africa/Bissau");
        tlist.add("Africa/Blantyre");
        tlist.add("Africa/Brazzaville");
        tlist.add("Africa/Bujumbura");
        tlist.add("Africa/Cairo");
        tlist.add("Africa/Casablanca");
        tlist.add("Africa/Ceuta");
        tlist.add("Africa/Conakry");
        tlist.add("Africa/Dakar");
        tlist.add("Africa/Dar_es_Salaam");
        tlist.add("Africa/Djibouti");
        tlist.add("Africa/Douala");
        tlist.add("Africa/El_Aaiun");
        tlist.add("Africa/Freetown");
        tlist.add("Africa/Gaborone");
        tlist.add("Africa/Harare");
        tlist.add("Africa/Johannesburg");
        tlist.add("Africa/Kampala");
        tlist.add("Africa/Khartoum");
        tlist.add("Africa/Kigali");
        tlist.add("Africa/Kinshasa");
        tlist.add("Africa/Lagos");
        tlist.add("Africa/Libreville");
        tlist.add("Africa/Lome");
        tlist.add("Africa/Luanda");
        tlist.add("Africa/Lubumbashi");
        tlist.add("Africa/Lusaka");
        tlist.add("Africa/Malabo");
        tlist.add("Africa/Maputo");
        tlist.add("Africa/Maseru");
        tlist.add("Africa/Mbabane");
        tlist.add("Africa/Mogadishu");
        tlist.add("Africa/Monrovia");
        tlist.add("Africa/Nairobi");
        tlist.add("Africa/Ndjamena");
        tlist.add("Africa/Niamey");
        tlist.add("Africa/Nouakchott");
        tlist.add("Africa/Ouagadougou");
        tlist.add("Africa/Porto-Novo");
        tlist.add("Africa/Sao_Tome");
        tlist.add("Africa/Timbuktu");
        tlist.add("Africa/Tripoli");
        tlist.add("Africa/Tunis");
        tlist.add("Africa/Windhoek");
        tlist.add("America/Adak");
        tlist.add("America/Anchorage");
        tlist.add("America/Anguilla");
        tlist.add("America/Antigua");
        tlist.add("America/Araguaina");
        tlist.add("America/Argentina/Buenos_Aires");
        tlist.add("America/Argentina/Catamarca");
        tlist.add("America/Argentina/ComodRivadavia");
        tlist.add("America/Argentina/Cordoba");
        tlist.add("America/Argentina/Jujuy");
        tlist.add("America/Argentina/La_Rioja");
        tlist.add("America/Argentina/Mendoza");
        tlist.add("America/Argentina/Rio_Gallegos");
        tlist.add("America/Argentina/Salta");
        tlist.add("America/Argentina/San_Juan");
        tlist.add("America/Argentina/San_Luis");
        tlist.add("America/Argentina/Tucuman");
        tlist.add("America/Argentina/Ushuaia");
        tlist.add("America/Aruba");
        tlist.add("America/Asuncion");
        tlist.add("America/Atikokan");
        tlist.add("America/Atka");
        tlist.add("America/Bahia");
        tlist.add("America/Barbados");
        tlist.add("America/Belem");
        tlist.add("America/Belize");
        tlist.add("America/Blanc-Sablon");
        tlist.add("America/Boa_Vista");
        tlist.add("America/Bogota");
        tlist.add("America/Boise");
        tlist.add("America/Buenos_Aires");
        tlist.add("America/Cambridge_Bay");
        tlist.add("America/Campo_Grande");
        tlist.add("America/Cancun");
        tlist.add("America/Caracas");
        tlist.add("America/Catamarca");
        tlist.add("America/Cayenne");
        tlist.add("America/Cayman");
        tlist.add("America/Chicago");
        tlist.add("America/Chihuahua");
        tlist.add("America/Coral_Harbour");
        tlist.add("America/Cordoba");
        tlist.add("America/Costa_Rica");
        tlist.add("America/Cuiaba");
        tlist.add("America/Curacao");
        tlist.add("America/Danmarkshavn");
        tlist.add("America/Dawson");
        tlist.add("America/Dawson_Creek");
        tlist.add("America/Denver");
        tlist.add("America/Detroit");
        tlist.add("America/Dominica");
        tlist.add("America/Edmonton");
        tlist.add("America/Eirunepe");
        tlist.add("America/El_Salvador");
        tlist.add("America/Ensenada");
        tlist.add("America/Fort_Wayne");
        tlist.add("America/Fortaleza");
        tlist.add("America/Glace_Bay");
        tlist.add("America/Godthab");
        tlist.add("America/Goose_Bay");
        tlist.add("America/Grand_Turk");
        tlist.add("America/Grenada");
        tlist.add("America/Guadeloupe");
        tlist.add("America/Guatemala");
        tlist.add("America/Guayaquil");
        tlist.add("America/Guyana");
        tlist.add("America/Halifax");
        tlist.add("America/Havana");
        tlist.add("America/Hermosillo");
        tlist.add("America/Indiana/Indianapolis");
        tlist.add("America/Indiana/Knox");
        tlist.add("America/Indiana/Marengo");
        tlist.add("America/Indiana/Petersburg");
        tlist.add("America/Indiana/Tell_City");
        tlist.add("America/Indiana/Vevay");
        tlist.add("America/Indiana/Vincennes");
        tlist.add("America/Indiana/Winamac");
        tlist.add("America/Indianapolis");
        tlist.add("America/Inuvik");
        tlist.add("America/Iqaluit");
        tlist.add("America/Jamaica");
        tlist.add("America/Jujuy");
        tlist.add("America/Juneau");
        tlist.add("America/Kentucky/Louisville");
        tlist.add("America/Kentucky/Monticello");
        tlist.add("America/Knox_IN");
        tlist.add("America/La_Paz");
        tlist.add("America/Lima");
        tlist.add("America/Los_Angeles");
        tlist.add("America/Louisville");
        tlist.add("America/Maceio");
        tlist.add("America/Managua");
        tlist.add("America/Manaus");
        tlist.add("America/Marigot");
        tlist.add("America/Martinique");
        tlist.add("America/Mazatlan");
        tlist.add("America/Mendoza");
        tlist.add("America/Menominee");
        tlist.add("America/Merida");
        tlist.add("America/Mexico_City");
        tlist.add("America/Miquelon");
        tlist.add("America/Moncton");
        tlist.add("America/Monterrey");
        tlist.add("America/Montevideo");
        tlist.add("America/Montreal");
        tlist.add("America/Montserrat");
        tlist.add("America/Nassau");
        tlist.add("America/New_York");
        tlist.add("America/Nipigon");
        tlist.add("America/Nome");
        tlist.add("America/Noronha");
        tlist.add("America/North_Dakota/Center");
        tlist.add("America/North_Dakota/New_Salem");
        tlist.add("America/Panama");
        tlist.add("America/Pangnirtung");
        tlist.add("America/Paramaribo");
        tlist.add("America/Phoenix");
        tlist.add("America/Port-au-Prince");
        tlist.add("America/Port_of_Spain");
        tlist.add("America/Porto_Acre");
        tlist.add("America/Porto_Velho");
        tlist.add("America/Puerto_Rico");
        tlist.add("America/Rainy_River");
        tlist.add("America/Rankin_Inlet");
        tlist.add("America/Recife");
        tlist.add("America/Regina");
        tlist.add("America/Resolute");
        tlist.add("America/Rio_Branco");
        tlist.add("America/Rosario");
        tlist.add("America/Santarem");
        tlist.add("America/Santiago");
        tlist.add("America/Santo_Domingo");
        tlist.add("America/Sao_Paulo");
        tlist.add("America/Scoresbysund");
        tlist.add("America/Shiprock");
        tlist.add("America/St_Barthelemy");
        tlist.add("America/St_Johns");
        tlist.add("America/St_Kitts");
        tlist.add("America/St_Lucia");
        tlist.add("America/St_Thomas");
        tlist.add("America/St_Vincent");
        tlist.add("America/Swift_Current");
        tlist.add("America/Tegucigalpa");
        tlist.add("America/Thule");
        tlist.add("America/Thunder_Bay");
        tlist.add("America/Tijuana");
        tlist.add("America/Toronto");
        tlist.add("America/Tortola");
        tlist.add("America/Vancouver");
        tlist.add("America/Virgin");
        tlist.add("America/Whitehorse");
        tlist.add("America/Winnipeg");
        tlist.add("America/Yakutat");
        tlist.add("America/Yellowknife");
        tlist.add("Antarctica/Casey");
        tlist.add("Antarctica/Davis");
        tlist.add("Antarctica/DumontDUrville");
        tlist.add("Antarctica/Mawson");
        tlist.add("Antarctica/McMurdo");
        tlist.add("Antarctica/Palmer");
        tlist.add("Antarctica/Rothera");
        tlist.add("Antarctica/South_Pole");
        tlist.add("Antarctica/Syowa");
        tlist.add("Antarctica/Vostok");
        tlist.add("Arctic/Longyearbyen");
        tlist.add("Asia/Aden");
        tlist.add("Asia/Almaty");
        tlist.add("Asia/Amman");
        tlist.add("Asia/Anadyr");
        tlist.add("Asia/Aqtau");
        tlist.add("Asia/Aqtobe");
        tlist.add("Asia/Ashgabat");
        tlist.add("Asia/Ashkhabad");
        tlist.add("Asia/Baghdad");
        tlist.add("Asia/Bahrain");
        tlist.add("Asia/Baku");
        tlist.add("Asia/Bangkok");
        tlist.add("Asia/Beirut");
        tlist.add("Asia/Bishkek");
        tlist.add("Asia/Brunei");
        tlist.add("Asia/Calcutta");
        tlist.add("Asia/Choibalsan");
        tlist.add("Asia/Chongqing");
        tlist.add("Asia/Chungking");
        tlist.add("Asia/Colombo");
        tlist.add("Asia/Dacca");
        tlist.add("Asia/Damascus");
        tlist.add("Asia/Dhaka");
        tlist.add("Asia/Dili");
        tlist.add("Asia/Dubai");
        tlist.add("Asia/Dushanbe");
        tlist.add("Asia/Gaza");
        tlist.add("Asia/Harbin");
        tlist.add("Asia/Ho_Chi_Minh");
        tlist.add("Asia/Hong_Kong");
        tlist.add("Asia/Hovd");
        tlist.add("Asia/Irkutsk");
        tlist.add("Asia/Istanbul");
        tlist.add("Asia/Jakarta");
        tlist.add("Asia/Jayapura");
        tlist.add("Asia/Jerusalem");
        tlist.add("Asia/Kabul");
        tlist.add("Asia/Kamchatka");
        tlist.add("Asia/Karachi");
        tlist.add("Asia/Kashgar");
        tlist.add("Asia/Kathmandu");
        tlist.add("Asia/Katmandu");
        tlist.add("Asia/Kolkata");
        tlist.add("Asia/Krasnoyarsk");
        tlist.add("Asia/Kuala_Lumpur");
        tlist.add("Asia/Kuching");
        tlist.add("Asia/Kuwait");
        tlist.add("Asia/Macao");
        tlist.add("Asia/Macau");
        tlist.add("Asia/Magadan");
        tlist.add("Asia/Makassar");
        tlist.add("Asia/Manila");
        tlist.add("Asia/Muscat");
        tlist.add("Asia/Nicosia");
        tlist.add("Asia/Novokuznetsk");
        tlist.add("Asia/Novosibirsk");
        tlist.add("Asia/Omsk");
        tlist.add("Asia/Oral");
        tlist.add("Asia/Phnom_Penh");
        tlist.add("Asia/Pontianak");
        tlist.add("Asia/Pyongyang");
        tlist.add("Asia/Qatar");
        tlist.add("Asia/Qyzylorda");
        tlist.add("Asia/Rangoon");
        tlist.add("Asia/Riyadh");
        tlist.add("Asia/Saigon");
        tlist.add("Asia/Sakhalin");
        tlist.add("Asia/Samarkand");
        tlist.add("Asia/Seoul");
        tlist.add("Asia/Shanghai");
        tlist.add("Asia/Singapore");
        tlist.add("Asia/Taipei");
        tlist.add("Asia/Tashkent");
        tlist.add("Asia/Tbilisi");
        tlist.add("Asia/Tehran");
        tlist.add("Asia/Tel_Aviv");
        tlist.add("Asia/Thimbu");
        tlist.add("Asia/Thimphu");
        tlist.add("Asia/Tokyo");
        tlist.add("Asia/Ujung_Pandang");
        tlist.add("Asia/Ulaanbaatar");
        tlist.add("Asia/Ulan_Bator");
        tlist.add("Asia/Urumqi");
        tlist.add("Asia/Vientiane");
        tlist.add("Asia/Vladivostok");
        tlist.add("Asia/Yakutsk");
        tlist.add("Asia/Yekaterinburg");
        tlist.add("Asia/Yerevan");
        tlist.add("Atlantic/Azores");
        tlist.add("Atlantic/Bermuda");
        tlist.add("Atlantic/Canary");
        tlist.add("Atlantic/Cape_Verde");
        tlist.add("Atlantic/Faeroe");
        tlist.add("Atlantic/Faroe");
        tlist.add("Atlantic/Jan_Mayen");
        tlist.add("Atlantic/Madeira");
        tlist.add("Atlantic/Reykjavik");
        tlist.add("Atlantic/South_Georgia");
        tlist.add("Atlantic/St_Helena");
        tlist.add("Atlantic/Stanley");
        tlist.add("Australia/ACT");
        tlist.add("Australia/Adelaide");
        tlist.add("Australia/Brisbane");
        tlist.add("Australia/Broken_Hill");
        tlist.add("Australia/Canberra");
        tlist.add("Australia/Currie");
        tlist.add("Australia/Darwin");
        tlist.add("Australia/Eucla");
        tlist.add("Australia/Hobart");
        tlist.add("Australia/LHI");
        tlist.add("Australia/Lindeman");
        tlist.add("Australia/Lord_Howe");
        tlist.add("Australia/Melbourne");
        tlist.add("Australia/NSW");
        tlist.add("Australia/North");
        tlist.add("Australia/Perth");
        tlist.add("Australia/Queensland");
        tlist.add("Australia/South");
        tlist.add("Australia/Sydney");
        tlist.add("Australia/Tasmania");
        tlist.add("Australia/Victoria");
        tlist.add("Australia/West");
        tlist.add("Australia/Yancowinna");
        tlist.add("Brazil/Acre");
        tlist.add("Brazil/DeNoronha");
        tlist.add("Brazil/East");
        tlist.add("Brazil/West");
        tlist.add("CET");
        tlist.add("CST6CDT");
        tlist.add("Canada/Atlantic");
        tlist.add("Canada/Central");
        tlist.add("Canada/East-Saskatchewan");
        tlist.add("Canada/Eastern");
        tlist.add("Canada/Mountain");
        tlist.add("Canada/Newfoundland");
        tlist.add("Canada/Pacific");
        tlist.add("Canada/Saskatchewan");
        tlist.add("Canada/Yukon");
        tlist.add("Chile/Continental");
        tlist.add("Chile/EasterIsland");
        tlist.add("Cuba");
        tlist.add("EET");
        tlist.add("EST");
        tlist.add("EST5EDT");
        tlist.add("Egypt");
        tlist.add("Eire");
        tlist.add("Etc/GMT");
        tlist.add("Etc/GMT+0");
        tlist.add("Etc/GMT+1");
        tlist.add("Etc/GMT+10");
        tlist.add("Etc/GMT+11");
        tlist.add("Etc/GMT+12");
        tlist.add("Etc/GMT+2");
        tlist.add("Etc/GMT+3");
        tlist.add("Etc/GMT+4");
        tlist.add("Etc/GMT+5");
        tlist.add("Etc/GMT+6");
        tlist.add("Etc/GMT+7");
        tlist.add("Etc/GMT+8");
        tlist.add("Etc/GMT+9");
        tlist.add("Etc/GMT-0");
        tlist.add("Etc/GMT-1");
        tlist.add("Etc/GMT-10");
        tlist.add("Etc/GMT-11");
        tlist.add("Etc/GMT-12");
        tlist.add("Etc/GMT-13");
        tlist.add("Etc/GMT-14");
        tlist.add("Etc/GMT-2");
        tlist.add("Etc/GMT-3");
        tlist.add("Etc/GMT-4");
        tlist.add("Etc/GMT-5");
        tlist.add("Etc/GMT-6");
        tlist.add("Etc/GMT-7");
        tlist.add("Etc/GMT-8");
        tlist.add("Etc/GMT-9");
        tlist.add("Etc/GMT0");
        tlist.add("Etc/Greenwich");
        tlist.add("Etc/UCT");
        tlist.add("Etc/UTC");
        tlist.add("Etc/Universal");
        tlist.add("Etc/Zulu");
        tlist.add("Europe/Amsterdam");
        tlist.add("Europe/Andorra");
        tlist.add("Europe/Athens");
        tlist.add("Europe/Belfast");
        tlist.add("Europe/Belgrade");
        tlist.add("Europe/Berlin");
        tlist.add("Europe/Bratislava");
        tlist.add("Europe/Brussels");
        tlist.add("Europe/Bucharest");
        tlist.add("Europe/Budapest");
        tlist.add("Europe/Chisinau");
        tlist.add("Europe/Copenhagen");
        tlist.add("Europe/Dublin");
        tlist.add("Europe/Gibraltar");
        tlist.add("Europe/Guernsey");
        tlist.add("Europe/Helsinki");
        tlist.add("Europe/Isle_of_Man");
        tlist.add("Europe/Istanbul");
        tlist.add("Europe/Jersey");
        tlist.add("Europe/Kaliningrad");
        tlist.add("Europe/Kiev");
        tlist.add("Europe/Lisbon");
        tlist.add("Europe/Ljubljana");
        tlist.add("Europe/London");
        tlist.add("Europe/Luxembourg");
        tlist.add("Europe/Madrid");
        tlist.add("Europe/Malta");
        tlist.add("Europe/Mariehamn");
        tlist.add("Europe/Minsk");
        tlist.add("Europe/Monaco");
        tlist.add("Europe/Moscow");
        tlist.add("Europe/Nicosia");
        tlist.add("Europe/Oslo");
        tlist.add("Europe/Paris");
        tlist.add("Europe/Podgorica");
        tlist.add("Europe/Prague");
        tlist.add("Europe/Riga");
        tlist.add("Europe/Rome");
        tlist.add("Europe/Samara");
        tlist.add("Europe/San_Marino");
        tlist.add("Europe/Sarajevo");
        tlist.add("Europe/Simferopol");
        tlist.add("Europe/Skopje");
        tlist.add("Europe/Sofia");
        tlist.add("Europe/Stockholm");
        tlist.add("Europe/Tallinn");
        tlist.add("Europe/Tirane");
        tlist.add("Europe/Tiraspol");
        tlist.add("Europe/Uzhgorod");
        tlist.add("Europe/Vaduz");
        tlist.add("Europe/Vatican");
        tlist.add("Europe/Vienna");
        tlist.add("Europe/Vilnius");
        tlist.add("Europe/Volgograd");
        tlist.add("Europe/Warsaw");
        tlist.add("Europe/Zagreb");
        tlist.add("Europe/Zaporozhye");
        tlist.add("Europe/Zurich");
        tlist.add("Factory");
        tlist.add("GB");
        tlist.add("GB-Eire");
        tlist.add("GMT");
        tlist.add("GMT+0");
        tlist.add("GMT-0");
        tlist.add("GMT0");
        tlist.add("Greenwich");
        tlist.add("HST");
        tlist.add("Hongkong");
        tlist.add("Iceland");
        tlist.add("Indian/Antananarivo");
        tlist.add("Indian/Chagos");
        tlist.add("Indian/Christmas");
        tlist.add("Indian/Cocos");
        tlist.add("Indian/Comoro");
        tlist.add("Indian/Kerguelen");
        tlist.add("Indian/Mahe");
        tlist.add("Indian/Maldives");
        tlist.add("Indian/Mauritius");
        tlist.add("Indian/Mayotte");
        tlist.add("Indian/Reunion");
        tlist.add("Iran");
        tlist.add("Israel");
        tlist.add("Jamaica");
        tlist.add("Japan");
        tlist.add("Kwajalein");
        tlist.add("Libya");
        tlist.add("MET");
        tlist.add("MST");
        tlist.add("MST7MDT");
        tlist.add("Mexico/BajaNorte");
        tlist.add("Mexico/BajaSur");
        tlist.add("Mexico/General");
        tlist.add("NZ");
        tlist.add("NZ-CHAT");
        tlist.add("Navajo");
        tlist.add("PRC");
        tlist.add("PST8PDT");
        tlist.add("Pacific/Apia");
        tlist.add("Pacific/Auckland");
        tlist.add("Pacific/Chatham");
        tlist.add("Pacific/Easter");
        tlist.add("Pacific/Efate");
        tlist.add("Pacific/Enderbury");
        tlist.add("Pacific/Fakaofo");
        tlist.add("Pacific/Fiji");
        tlist.add("Pacific/Funafuti");
        tlist.add("Pacific/Galapagos");
        tlist.add("Pacific/Gambier");
        tlist.add("Pacific/Guadalcanal");
        tlist.add("Pacific/Guam");
        tlist.add("Pacific/Honolulu");
        tlist.add("Pacific/Johnston");
        tlist.add("Pacific/Kiritimati");
        tlist.add("Pacific/Kosrae");
        tlist.add("Pacific/Kwajalein");
        tlist.add("Pacific/Majuro");
        tlist.add("Pacific/Marquesas");
        tlist.add("Pacific/Midway");
        tlist.add("Pacific/Nauru");
        tlist.add("Pacific/Niue");
        tlist.add("Pacific/Norfolk");
        tlist.add("Pacific/Noumea");
        tlist.add("Pacific/Pago_Pago");
        tlist.add("Pacific/Palau");
        tlist.add("Pacific/Pitcairn");
        tlist.add("Pacific/Ponape");
        tlist.add("Pacific/Port_Moresby");
        tlist.add("Pacific/Rarotonga");
        tlist.add("Pacific/Saipan");
        tlist.add("Pacific/Samoa");
        tlist.add("Pacific/Tahiti");
        tlist.add("Pacific/Tarawa");
        tlist.add("Pacific/Tongatapu");
        tlist.add("Pacific/Truk");
        tlist.add("Pacific/Wake");
        tlist.add("Pacific/Wallis");
        tlist.add("Pacific/Yap");
        tlist.add("Poland");
        tlist.add("Portugal");
        tlist.add("ROC");
        tlist.add("ROK");
        tlist.add("Singapore");
        tlist.add("Turkey");
        tlist.add("UCT");
        tlist.add("US/Alaska");
        tlist.add("US/Aleutian");
        tlist.add("US/Arizona");
        tlist.add("US/Central");
        tlist.add("US/East-Indiana");
        tlist.add("US/Eastern");
        tlist.add("US/Hawaii");
        tlist.add("US/Indiana-Starke");
        tlist.add("US/Michigan");
        tlist.add("US/Mountain");
        tlist.add("US/Pacific");
        tlist.add("US/Pacific-New");
        tlist.add("US/Samoa");
        tlist.add("UTC");
        tlist.add("Universal");
        tlist.add("W-SU");
        tlist.add("WET");
        tlist.add("Zulu");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_model , tlist);

        timezone.setAdapter(adapter);

        timezone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                tzone = tlist.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void signup() {

        signupRequestBean body = new signupRequestBean();
        body.setAction("register");
        Data data = new Data();

        data.setEmail(syntax_email);
        data.setName(syntax_name);
        data.setPassword(syntax_password);
        data.setPhone(syntax_phone);
        data.setTzone(tzone);
        body.setData(data);

        // Call<signupResponseBean> call =



        Call<signupResponseBean> call = serviceInterface.signup(body);

        call.enqueue(new Callback<signupResponseBean>() {
            @Override
            public void onResponse(Call<signupResponseBean> call, Response<signupResponseBean> response) {
                if (response.body().getStatus().equals("1"))
                {
                   pBar.setVisibility(View.GONE);
                    Toast.makeText(SignupActivity.this, ""+response.body().getData().getName(), Toast.LENGTH_SHORT).show();

                    SharePreferenceUtils.getInstance().saveString(Constant.USER_id,response.body().getData().getUserId());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_email,response.body().getData().getEmail());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_name,response.body().getData().getName());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_phone,response.body().getData().getMobile());
                    Intent homeIntent = new Intent(SignupActivity.this, HomeActivity2.class);
                    startActivity(homeIntent);
                    finishAffinity();

                   /* Intent signinIntent = new Intent(SignupActivity.this,SigninActivity.class);
                    startActivity(signinIntent);*/
                }
                else
                {
                    pBar.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(rootlayout,"Signup Failed Already Registered",Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.seaGreen));
                    snackbar.show();
                }

            }

            @Override
            public void onFailure(Call<signupResponseBean> call, Throwable t) {
                pBar.setVisibility(View.GONE);
                Toast.makeText(SignupActivity.this, ""+"failed", Toast.LENGTH_SHORT).show();

            }
        });




    }


    private void setUpWidget() {

        syntaxName = findViewById(R.id.syntax_name);
        syntaxEmail = findViewById(R.id.syntax_email);
        syntaxPhone =findViewById(R.id.syntax_phone);
        syntaxPassword =  findViewById(R.id.syntax_password);
        syntaxRePassword = findViewById(R.id.syntax_rePassword);
        syntaxSignupBtn = findViewById(R.id.syntax_signupBtn);
        syntaxSigninNow = findViewById(R.id.syntax_signinNow);
        rootlayout = findViewById(R.id.rootlayout);
        timezone = findViewById(R.id.timezone);

        pBar = findViewById(R.id.progressBar);


    }
    private void getData() {
        syntax_name = syntaxName.getText().toString().trim();
        syntax_email = syntaxEmail.getText().toString().trim();
        syntax_password = syntaxPassword.getText().toString().trim();
        syntax_repassword = syntaxRePassword.getText().toString().trim();
        syntax_phone = syntaxPhone.getText().toString().trim();
    }


    private void dataValidation() {
        isValid = true;

        if (syntaxName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Invalid Name", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else {

        }

        if (syntaxEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else {

        }

        if (syntaxPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else {

        }
        if (!syntaxRePassword.getText().toString().equals(syntaxPassword.getText().toString())) {
            Toast.makeText(this, "Passwords did not match", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else {

        }

        if (syntaxPhone.getText().toString().isEmpty()) {
            Toast.makeText(this, "Invalid Phone", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else {

        }



        if (isValid) {
            //  Toast.makeText(AddPropertyActivity.this, "Datum Verified", Toast.LENGTH_SHORT).show();
        }
    }

}
