package org.tron.client;

import static org.tron.client.SideChainGatewayApi.GatewayApi.GATEWAY_API;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.tron.common.config.Args;
import org.tron.common.exception.RpcConnectException;
import org.tron.common.exception.TxRollbackException;
import org.tron.common.exception.TxValidateException;
import org.tron.common.utils.AbiUtil;
import org.tron.protos.Protocol.Transaction;
import org.tron.service.check.TransactionExtention;

@Slf4j
public class SideChainGatewayApi {

  public static TransactionExtention mintTrx(String to, String value) throws RpcConnectException {
    byte[] contractAddress = Args.getInstance().getSidechainGateway();
    String method = "depositTRX(address,uint256)";
    List params = Arrays.asList(to, value);
    return GATEWAY_API.getInstance().triggerContract(contractAddress, method, params, 0, 0, 0);
  }

  public static TransactionExtention mintToken10(String to, String tokenId, String value,
      String name,
      String symbol, int decimals)
      throws RpcConnectException {
    byte[] contractAddress = Args.getInstance().getSidechainGateway();
    String method = "depositTRC10(address,uint256,uint256,string,string,uint8)";
    List params = Arrays.asList(to, tokenId, value, name, symbol, decimals);
    return GATEWAY_API.getInstance().triggerContract(contractAddress, method, params, 0, 0, 0);
  }

  public static TransactionExtention mintToken20(String to, String mainAddress, String value)
      throws RpcConnectException {
    byte[] contractAddress = Args.getInstance().getSidechainGateway();
    String method = "depositTRC20(address,address,uint256)";
    List params = Arrays.asList(to, mainAddress, value);
    return GATEWAY_API.getInstance().triggerContract(contractAddress, method, params, 0, 0, 0);
  }

  public static TransactionExtention mintToken721(String to, String mainAddress, String value)
      throws RpcConnectException {
    byte[] contractAddress = Args.getInstance().getSidechainGateway();
    String method = "depositTRC721(address,address,uint256)";
    List params = Arrays.asList(to, mainAddress, value);
    return GATEWAY_API.getInstance().triggerContract(contractAddress, method, params, 0, 0, 0);
  }

  // Singleton
  enum GatewayApi {
    GATEWAY_API;

    private WalletClient instance;

    GatewayApi() {
      instance = new WalletClient(Args.getInstance().getSidechainFullNode(),
          Args.getInstance().getOraclePrivateKey());
    }

    public WalletClient getInstance() {
      return instance;
    }

  }

  public static String getMainToSideContractMap(String address) throws RpcConnectException {
    byte[] contractAddress = Args.getInstance().getSidechainGateway();
    String method = "mainToSideContractMap(address)";
    List params = Arrays.asList(address);
    byte[] ret = GATEWAY_API.getInstance()
        .triggerConstantContractAndReturn(contractAddress, method, params, 0, 0, 0);
    return AbiUtil.unpackAddress(ret);
  }

  public static String getSunTokenAddress() throws RpcConnectException {
    byte[] contractAddress = Args.getInstance().getSidechainGateway();
    String method = "sunTokenAddress()";
    List<Object> params = new ArrayList<>();
    byte[] ret = GATEWAY_API.getInstance()
        .triggerConstantContractAndReturn(contractAddress, method, params, 0, 0, 0);
    return AbiUtil.unpackAddress(ret);
  }

  public static String getMainToSideTRC10Map(String tokenId) throws RpcConnectException {
    byte[] contractAddress = Args.getInstance().getMainchainGateway();
    String method = "mainToSideTRC10Map(uint256)";
    List params = Arrays.asList(tokenId);
    byte[] ret = MainChainGatewayApi.GatewayApi.GATEWAY_API.getInstance()
        .triggerConstantContractAndReturn(contractAddress, method, params, 0, 0, 0);
    return AbiUtil.unpackAddress(ret);
  }


  public static byte[] checkTxInfo(TransactionExtention txId)
      throws TxValidateException, TxRollbackException {
    return GATEWAY_API.instance.checkTxInfo(txId.getTransactionId());
  }

  public static boolean broadcast(Transaction transaction) {
    return GATEWAY_API.instance.broadcast(transaction);
  }
}
