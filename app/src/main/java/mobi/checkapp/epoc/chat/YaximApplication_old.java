package mobi.checkapp.epoc.chat;

/**
public class YaximApplication_old extends Application {
	// identity name and type, see:
	// http://xmpp.org/registrar/disco-categories.html
	public static final String XMPP_IDENTITY_NAME = "chat_respira";
	public static final String XMPP_IDENTITY_TYPE = "phone";

	// MTM is needed globally for both the backend (connect)
	// and the frontend (display dialog)
	public MemorizingTrustManager mMTM;

	private YaximConfiguration mConfig;

	public YaximApplication_old() {
		super();
	}

	@Override
	public void onCreate() {
		mMTM = new MemorizingTrustManager(this);
		mConfig = new YaximConfiguration(PreferenceManager
				.getDefaultSharedPreferences(this));
	}

	public static YaximApplication_old getApp(Context ctx) {
		return (YaximApplication_old)ctx.getApplicationContext();
	}

	public static YaximConfiguration getConfig(Context ctx) {
		return getApp(ctx).mConfig;
	}
}
*/
