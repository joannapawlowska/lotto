package pl.fissst.lbd.lambda.handler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.fissst.lbd.damagereport.model.CouponApi;
import pl.fissst.lbd.lambda.mapper.InvalidS3ObjectContentException;
import pl.fissst.lbd.lambda.mapper.S3ObjectContentMapper;

import java.util.Set;

class S3ObjectContentMapperTest {

    @Test
    void shouldMapWhenValidCoupon() {

        //FOR
        String bet = "20,3,5,2,4,1";

        //WHEN
        CouponApi result = S3ObjectContentMapper.mapToCouponApi(bet);

        //THEN
        Assertions.assertEquals(Set.of(20, 3, 5, 2, 4, 1), result.getNumbers());
    }

    @Test
    void shouldThrowWhenDuplicate() {

        //FOR
        String bet = "20,3,4,2,4,1";

        //THEN
        Assertions.assertThrows(InvalidS3ObjectContentException.class, () -> S3ObjectContentMapper.mapToCouponApi(bet));
    }

    @Test
    void shouldThrowWhenLetter() {

        //FOR
        String bet = "20,3,4,2,4,g";

        //THEN
        Assertions.assertThrows(NumberFormatException.class, () -> S3ObjectContentMapper.mapToCouponApi(bet));
    }

    @Test
    void shouldMapWhenSpace() {

        //FOR
        String bet = "20, 3, 7, 2, 4, 1";

        //WHEN
        CouponApi result = S3ObjectContentMapper.mapToCouponApi(bet);

        //THEN
        Assertions.assertEquals(Set.of(20, 3, 7, 2, 4, 1), result.getNumbers());
    }
}