import java.text.DecimalFormat;

class NewPosition {
	String ticker, secType, oldPrice, newPrice, callOrPut;
	
	public NewPosition (String ticker, double oldPrice, String secType, double newPrice, String callOrPut) {
		this.ticker = this.normalizeTicker(ticker);
		this.oldPrice = this.normalizePrice(oldPrice);
		this.secType = this.normalizeSecType(secType);
		this.newPrice = this.normalizePrice(newPrice);
		this.callOrPut = this.normalizeCallOrPut(callOrPut);
	}
	
	private String normalizePrice(double price) {
		DecimalFormat df = new DecimalFormat("#.####");
		String priceStr = Double.toString(Double.valueOf(df.format(price)));
		
		int lengthOfPrice = priceStr.substring(priceStr.indexOf(".")).length();
		for(int i = 0; i < 7 - lengthOfPrice; i++) {
			priceStr += "0";
		}
		priceStr = priceStr.replace(".", "");
		priceStr = ("000000000000" + priceStr).substring(priceStr.length());
		return priceStr;
	}

	private String normalizeCallOrPut(String callOrPut) {
		if (callOrPut != null) {
			return callOrPut.substring(0, 1);
		}
		return callOrPut;
	}

	private String normalizeSecType(String secType) {
		if (secType != null) {
			return secType.toUpperCase();
		}
		return secType;
	}

	private String normalizeTicker(String ticker) {
		if(ticker != null && ticker.length() > 6) {
			return ticker.substring(0, 6);
		} else if (ticker == null) {
			return "";
		}
		return ticker;
	}

	public String getTicker() {
		return ticker;
	}
	
	public String getSecType() {
		return secType;
	}
	
	public String getCallOrPut() {
		return callOrPut;
	}

	public String getOldPrice() {
		return oldPrice;
	}

	public String getNewPrice() {
		return newPrice;
	}

}
