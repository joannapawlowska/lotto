//package pl.fissst.lbd.lambda.handler;
//
//import org.junit.jupiter.api.Assertions;
//
//class S3ObjectContentMapperTest {
//
//    @org.junit.jupiter.api.Test
//    void isValidBet() {
//
//        //FOR
//        String bet = "20,3,5,2,4,1";
//
//        //WHEN
//        boolean result = S3ObjectContentMapper.isValidBet(bet);
//
//        //THEN
//        Assertions.assertTrue(result);
//    }
//
//    @org.junit.jupiter.api.Test
//    void isInvalidBetWhenDuplicate() {
//
//        //FOR
//        String bet = "20,3,4,2,4,1";
//
//        //WHEN
//        boolean result = S3ObjectContentMapper.isValidBet(bet);
//
//        //THEN
//        Assertions.assertFalse(result);
//    }
//
//    @org.junit.jupiter.api.Test
//    void isValidBetWhenLetter() {
//
//        //FOR
//        String bet = "20,3,4,2,4,g";
//
//        //WHEN
//        boolean result = S3ObjectContentMapper.isValidBet(bet);
//
//        //THEN
//        Assertions.assertFalse(result);
//    }
//
//    @org.junit.jupiter.api.Test
//    void isValidBetWhenSpace() {
//
//        //FOR
//        String bet = "20, 3, 7, 2, 4, 1";
//
//        //WHEN
//        boolean result = S3ObjectContentMapper.isValidBet(bet);
//
//        //THEN
//        Assertions.assertTrue(result);
//    }
//}