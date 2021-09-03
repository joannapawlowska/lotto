package pl.fissst.lbd.lambda.mapper;

import com.amazonaws.services.s3.model.S3Object;
import pl.fissst.lbd.damagereport.model.BetApi;
import pl.fissst.lbd.damagereport.model.CouponApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class S3ObjectContentMapper {

    public static BetApi mapToBetApi(S3Object s3Object) throws InvalidS3ObjectContentException {

        try (InputStream stream = s3Object.getObjectContent()) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            List<String> rawCoupons = retrieveRawCoupons(reader);

            List<CouponApi> coupons = rawCoupons.stream()
                    .map(S3ObjectContentMapper::mapToCouponApi)
                    .collect(Collectors.toList());

            return mapToBetApi(coupons);

        } catch (IOException | NumberFormatException e) {
            throw new InvalidS3ObjectContentException();
        }
    }

    private static List<String> retrieveRawCoupons(BufferedReader reader) {
        return reader.lines()
                .skip(2)
                .collect(Collectors.toList());
    }

    static CouponApi mapToCouponApi(String coupon) throws NumberFormatException {
        Set<Integer> numbers = Arrays.stream(coupon.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .filter(n -> n > 0 && n < 50)
                .collect(Collectors.toSet());

        if (numbers.size() != 6)
            throw new InvalidS3ObjectContentException();

        return createNewCouponApi(numbers);
    }

    static CouponApi createNewCouponApi(Set<Integer> numbers) {
        CouponApi couponApi = new CouponApi();
        couponApi.setNumbers(numbers);
        return couponApi;
    }

    private static BetApi mapToBetApi(List<CouponApi> coupons) {
        int couponModelsNumber = coupons.size();

        if (couponModelsNumber < 1)
            throw new InvalidS3ObjectContentException();

        return createNewBetApi(coupons);
    }

    static BetApi createNewBetApi(List<CouponApi> coupons) {
        BetApi betApi = new BetApi();
        betApi.setCoupons(coupons);
        return betApi;
    }
}