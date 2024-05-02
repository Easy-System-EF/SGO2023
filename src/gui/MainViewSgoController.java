package gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.MainSgo;
import gui.copia.Copia;
import gui.copia.CopiaService;
import gui.copia.CopiaSgoController;
import gui.copia.Restaura;
import gui.copia.RestauraService;
import gui.copia.RestauraSgoController;
import gui.listerneres.DataChangeListener;
import gui.sgcp.CompromissoCadastroListController;
import gui.sgcp.FornecedorCadastroListController;
import gui.sgcp.ParcelaConsultaListAbertoController;
import gui.sgcp.ParcelaConsultaListPagoController;
import gui.sgcp.ParcelaPrintRelatorioAbertoController;
import gui.sgcp.ParcelaPrintRelatorioPagoController;
import gui.sgcp.TipoConsumoCadastroListController;
import gui.sgcpmodel.services.CompromissoService;
import gui.sgcpmodel.services.FornecedorService;
import gui.sgcpmodel.services.ParPeriodoService;
import gui.sgcpmodel.services.ParcelaService;
import gui.sgcpmodel.services.TipoConsumoService;
import gui.sgo.AdiantamentoCadastroListController;
import gui.sgo.BalcaoCadastroListController;
import gui.sgo.CargoCadastroListController;
import gui.sgo.ClienteCadastroListController;
import gui.sgo.ClienteMVRConsultaListController;
import gui.sgo.ComissaoConsultaListController;
import gui.sgo.EntradaCadastroListController;
import gui.sgo.FechamentoAnualConsultaListController;
import gui.sgo.FechamentoMesConsultaListController;
import gui.sgo.FolhaMesConsultaListController;
import gui.sgo.FuncionarioCadastroListController;
import gui.sgo.GrupoCadastroListController;
import gui.sgo.LoginFormController;
import gui.sgo.MaterialCadastroListController;
import gui.sgo.MaterialMVRConsultaListController;
import gui.sgo.OrcamentoCadastroListController;
import gui.sgo.OrdemServicoCadastroListController;
import gui.sgo.ReceberConsultaListAbertoController;
import gui.sgo.ReceberConsultaListPagoController;
import gui.sgo.ReceberRelatorioAbertoController;
import gui.sgo.ReceberRelatorioPagoController;
import gui.sgo.ReposicaoVeiculoConsultaListController;
import gui.sgo.UltimasVisitasConsultaListController;
import gui.sgomodel.entities.Login;
import gui.sgomodel.services.AdiantamentoService;
import gui.sgomodel.services.BalcaoService;
import gui.sgomodel.services.CargoService;
import gui.sgomodel.services.ClienteService;
import gui.sgomodel.services.ComissaoService;
import gui.sgomodel.services.EntradaService;
import gui.sgomodel.services.FechamentoAnualService;
import gui.sgomodel.services.FechamentoMesService;
import gui.sgomodel.services.FolhaMesService;
import gui.sgomodel.services.FuncionarioService;
import gui.sgomodel.services.GrupoService;
import gui.sgomodel.services.LoginService;
import gui.sgomodel.services.MaterialService;
import gui.sgomodel.services.OrcVirtualService;
import gui.sgomodel.services.OrcamentoService;
import gui.sgomodel.services.OrdemServicoService;
import gui.sgomodel.services.ReceberService;
import gui.sgomodel.services.ReposicaoVeiculoService;
import gui.sgomodel.services.VeiculoService;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainViewSgoController implements Initializable, DataChangeListener {
	
	@FXML
	private MenuItem menuItemFornecedor;

	@FXML
	private MenuItem menuItemCompromisso;

	@FXML
	private MenuItem menuItemTipoConsumo;

	@FXML
	private MenuItem menuItemCliente;

	@FXML
	private MenuItem menuItemGrupo;

	@FXML
	private MenuItem menuItemMaterial;

	@FXML
	private MenuItem menuItemCargo;
	
	@FXML
	private MenuItem menuItemFuncionario;
	
	@FXML
	private MenuItem menuItemAdiantamento;
	
	@FXML
	private MenuItem menuItemEntrada;
	
	@FXML
	private MenuItem menuItemOrcamento;
	
	@FXML
	private MenuItem menuItemOrdemServico;
	
	@FXML
	private MenuItem menuItemBalcao;
	
	@FXML
	private MenuItem menuItemRelatorioParcelaAberto;

	@FXML
	private MenuItem menuItemRelatorioParcelaPago;

	@FXML
	private MenuItem menuItemRelatorioReceber;

	@FXML
	private MenuItem menuItemRelatorioRecebido;

	@FXML
	private MenuItem menuItemConsultaAberto;

	@FXML
	private MenuItem menuItemConsultaPago;

	@FXML
	private MenuItem menuItemConsultaReceber;
	
	@FXML
	private MenuItem menuItemConsultaRecebido;
	
	@FXML
	private MenuItem menuItemConsultaReposicao;
	
	@FXML
	private MenuItem menuItemConsultaVisitas;
	
	@FXML
	private MenuItem menuItemConsultaComissao;
	
	@FXML
	private MenuItem menuItemConsultaFolhaMes;
	
	@FXML
	private MenuItem menuItemConsultaFechamentoMes;
	
	@FXML
	private MenuItem menuItemConsultaFechamentoAno;
	
	@FXML
	private MenuItem menuItemConsultaClienteMVRList;
	
	@FXML
	private MenuItem menuItemConsultaMaterialMVRList;
	
	@FXML
	private MenuItem menuItemBackUp;

	@FXML
	private MenuItem menuItemRestaura;

	@FXML
	private MenuItem menuItemSobre;

	@FXML
	private Button btLogin;

	@FXML
	private Label labelUser;

//auxiliares	
	String classe = "";
	public static String senha = "null";
	public static int nivel = 0;
	public static String user = "usuário";	
//c�digo da empresa
//  1 = Easy; 2 = WS........	
	public static int numEmp = 2;
	
	@FXML
	private void onBtLoginAction() {
		classe = "Login ";
		Login log = new Login();
		senha = "null";
		createDialogForm(log, "/gui/sgo/LoginForm.fxml");
		if (senha == "null") {
			temLogar();
		}
		initializeNodes();
	}
	
	/*
	 * fun��o e inicializa��o do FornecedorController antes na loadview 2 -
	 * express�o lambda inicializa��o como parametro da fun��o loadView aqui
	 */
	@SuppressWarnings("static-access")
	@FXML
	public void onMenuItemFornecedorAction() {
		classe = "Fornecedor ";
		if (senha != "Ok") {
			temLogar();
		} else {	
		loadView("/gui/sgcp/FornecedorCadastroList.fxml", (FornecedorCadastroListController controller) -> {
			controller.user = user;
			controller.nivel = nivel;
			controller.setFornecedorService(new FornecedorService());
			controller.updateTableView();
			// view2 p/ funcionar mock
		});}
	}
	
	// criou um express�o lambda como par ametros para atz tableview =>
	// initializingAction
	@FXML
	public void onMenuItemCompromissoAction() {
		classe = "Compromisso ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9 || nivel == 2) {
				loadView("/gui/sgcp/CompromissoCadastroList.fxml", (CompromissoCadastroListController controller) -> {
					controller.user = user;
					controller.nivel = nivel;
					controller.setServices(new CompromissoService(), new ParcelaService());
					controller.updateTableView();
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}

	@FXML
	public void onMenuItemTipoConsumoAction() {
		classe = "Tipo Fornecedor ";
		if (senha != "Ok") {
			temLogar();
		} else {	
		loadView("/gui/sgcp/TipoConsumoCadastroList.fxml", (TipoConsumoCadastroListController controller) -> {
			controller.user = user;
			controller.nivel = nivel;
			controller.setTipoConsumoService(new TipoConsumoService());
			controller.updateTableView();
			// view2 p/ funcionar mock
		});}
	}

	@SuppressWarnings("static-access")
	@FXML
	public void onMenuItemClienteAction() {
		classe = "Cliente ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9 || nivel == 2) {
				loadView("/gui/sgo/ClienteCadastroList.fxml",  (ClienteCadastroListController controller) -> {
					controller.user = user;
					controller.nivel = nivel;
					controller.setClienteService(new ClienteService());
					controller.updateTableView();
		});
			}
		}	
	}

	@FXML
	public void onMenuItemGrupoAction() {
		classe = "Grupo ";
		if (senha != "Ok") {
			temLogar();
		} else {	
		loadView("/gui/sgo/GrupoCadastroList.fxml", (GrupoCadastroListController controller) -> {
			controller.user = user;
			controller.nivel = nivel;
			controller.setGrupoService(new GrupoService());
			controller.updateTableView();
		});}
	}
 
	@SuppressWarnings("static-access")
	@FXML
	public void onMenuItemMaterialAction() {
		classe = "Material ";
		if (senha != "Ok") {
			temLogar();
		} else {	
 		loadView("/gui/sgo/MaterialCadastroList.fxml", (MaterialCadastroListController controller) -> {
			controller.user = user;
			controller.nivel = nivel;
  			controller.setMaterialService(new MaterialService());
   			controller.updateTableView();
		});}
	}
 
	@FXML
	public void onMenuItemCargoAction() {
		classe = "Cargo ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgo/CargoCadastroList.fxml", (CargoCadastroListController controller) -> {
					controller.user = user;
					controller.nivel = nivel;
					controller.setCargoService(new CargoService());
					controller.updateTableView();
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}
 
	@FXML
	public void onMenuItemFuncionarioAction() {
		classe = "Funcionario ";
		if (senha != "Ok") {
			temLogar();
		} else {	
 		loadView("/gui/sgo/FuncionarioCadastroList.fxml", (FuncionarioCadastroListController controller) -> {
			controller.user = user;
  			controller.setFuncionarioService(new FuncionarioService());
   			controller.updateTableView();
		});}
	}
  
	@SuppressWarnings("static-access")
	@FXML
	public void onMenuItemAdiantamentoAction() {
		classe = "Adiantamento ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9 || nivel == 2) {
				loadView("/gui/sgo/AdiantamentoCadastroList.fxml", (AdiantamentoCadastroListController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.nivel = nivel;
					controller.setAdiantamentoService(new AdiantamentoService(), new CompromissoService(), new ParcelaService());
					controller.setTipo("A");
					controller.updateTableView();
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}
  
	@FXML
	public void onMenuItemEntradaAction() {
		classe = "Entrada ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9 || nivel == 2) {
				loadView("/gui/sgo/EntradaCadastroList.fxml", (EntradaCadastroListController controller) -> {
					controller.user = user;
					controller.nivel = nivel;
					controller.setServices(new MaterialService(), new EntradaService());
					controller.updateTableView();
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}
  
	@SuppressWarnings("static-access")
	@FXML
	public void onMenuItemOrcamentoAction() {
		classe = "Orçamento ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9 || nivel == 2) {
				loadView("/gui/sgo/OrcamentoCadastroList.fxml", (OrcamentoCadastroListController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.nivel = nivel;
					controller.setServices(new OrcamentoService(), new OrcVirtualService());
					controller.updateTableView();
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}
  
	@SuppressWarnings("static-access")
	@FXML
	public void onMenuItemOrdemServicoAction() {
		classe = "OS";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9 || nivel == 2) {
				loadView("/gui/sgo/OrdemServicoCadastroList.fxml", (OrdemServicoCadastroListController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.nivel = nivel;
					controller.setServices(new OrdemServicoService(), new ReceberService(), new ReposicaoVeiculoService(),
							new OrcamentoService(), new OrcVirtualService(), new MaterialService(), new VeiculoService(),
							new ComissaoService());
					controller.updateTableView();
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}
  
	@SuppressWarnings("static-access")
	@FXML
	public void onMenuItemBalcaoAction() {
		classe = "Balcão";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			loadView("/gui/sgo/BalcaoCadastroList.fxml", (BalcaoCadastroListController controller) -> {
				controller.user = user;
				controller.numEmp = numEmp;
				controller.nivel = nivel;
				controller.setServices(new BalcaoService(), 
									   new MaterialService(),
									   new OrcVirtualService(),
									   new ReceberService(),
									   new ComissaoService());
				controller.updateTableView();
			});
		}	
	}
  
	@SuppressWarnings("static-access")
	@FXML  
	public void onMenuItemRelatorioParcelaAbertoAction() {
		classe = "Relatório Compromisso Aberto ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgcp/ParcelaPrintRelatorioAberto.fxml", (ParcelaPrintRelatorioAbertoController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.setServices(new ParPeriodoService(), new ParcelaService());
					controller.updateTableViewAberto();
		}); 
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}

	@SuppressWarnings("static-access")
	@FXML
	public void onMenuItemRelatorioParcelaPagoAction() {
		classe = "Relatório Compromisso Pago ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgcp/ParcelaPrintRelatorioPago.fxml", (ParcelaPrintRelatorioPagoController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.setServices(new ParPeriodoService(), new ParcelaService());
					controller.updateTableViewPago();
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}
	}

	@SuppressWarnings("static-access")
	@FXML  
	public void onMenuItemRelatorioReceberAction() {
		classe = "Relatório Receber ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgo/ReceberRelatorioAberto.fxml", (ReceberRelatorioAbertoController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.setReceberService(new ReceberService());
					controller.updateTableViewAberto();
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}

	@SuppressWarnings("static-access")
	@FXML  
	public void onMenuItemRelatorioRecebidoAction() {
		classe = "Relatório Recebido ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgo/ReceberRelatorioPago.fxml", (ReceberRelatorioPagoController controller) -> {
					controller.user = user;
					controller.numEmp = numEmp;
					controller.opcao = 't';
					controller.setReceberService(new ReceberService());
					controller.updateTableViewPago();
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}

	@FXML
	public void onMenuItemConsultaAbertoAction() {
		classe = "Consulta Contas a Pagar ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgcp/ParcelaConsultaListAberto.fxml", (ParcelaConsultaListAbertoController controller) -> {
					controller.user = user;
					controller.setServices(new ParPeriodoService(), new ParcelaService());
					controller.updateTableViewAberto();
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}

	@FXML
	public void onMenuItemConsultaPagoAction() {
		classe = "Consulta Contas Pagas ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgcp/ParcelaConsultaListPago.fxml", (ParcelaConsultaListPagoController controller) -> {
					controller.user = user;
					controller.setParPeriodoService(new ParPeriodoService());
					controller.setParcelaService(new ParcelaService());
					controller.updateTableViewPago();
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}

	@FXML
	public void onMenuItemConsultaReceberAction() {
		classe = "Consulta Receber";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgo/ReceberConsultaListAberto.fxml", (ReceberConsultaListAbertoController controller) -> {
					controller.user = user;
					controller.setReceberService(new ReceberService());
					controller.updateTableView();
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}
  
	@FXML
	public void onMenuItemConsultaRecebidoAction() {
		classe = "Consulta Recebido ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgo/ReceberConsultaListPago.fxml", (ReceberConsultaListPagoController controller) -> {
					controller.user = user;
					controller.setReceberService(new ReceberService());
					controller.updateTableView();
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}
  
	@FXML
	public void onMenuItemConsultaReposicaoAction() {
		classe = "Consulta Reposição ";
		if (senha != "Ok") {
			temLogar();
		} else {	
 		loadView("/gui/sgo/ReposicaoVeiculoConsultaList.fxml", (ReposicaoVeiculoConsultaListController contRV) -> {
			contRV.user = user;
			contRV.nivel = nivel;
  			contRV.setReposicaoVeiculoService(new ReposicaoVeiculoService());
   			contRV.updateTableView();
		});}
	}
  
	@SuppressWarnings("static-access")
	@FXML
	public void onMenuItemConsultaVisitasAction() {
		classe = "Consulta Últimas Visitas Main ";
		if (senha != "Ok") {
			temLogar();
		} else {	
 		loadView("/gui/sgo/UltimasVisitasConsultaList.fxml", (UltimasVisitasConsultaListController contUV) -> {
			contUV.user = user;
			contUV.numEmp = numEmp;
  			contUV.setOrdemServicoService(new OrdemServicoService());
   			contUV.updateTableView();
		});}
	}
  
	@FXML
	public void onMenuItemConsultaComissaoAction() {
		classe = "Consulta Comissão ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgo/ComissaoConsultaList.fxml", (ComissaoConsultaListController controller) -> {
					controller.user = user;
					controller.setServices(new ComissaoService());
					controller.updateTableView();
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}
  
	@FXML
	public void onMenuItemConsultaFolhaMesAction() {
		classe = "Consulta Folha Mes ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgo/FolhaMesConsultaList.fxml", (FolhaMesConsultaListController controller) -> {
					controller.user = user;
					controller.setServices(new FolhaMesService());
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}
  
	@FXML
	public void onMenuItemConsultaFechamentoMesAction() {
		classe = "Consulta Fechamento Mes ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgo/FechamentoMesConsultaList.fxml", (FechamentoMesConsultaListController controller) -> {
					controller.user = user;
					controller.setServices(new FechamentoMesService());
		   			controller.updateTableView();
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}
  
	@FXML
	public void onMenuItemConsultaFechamentoAnualAction() {
		classe = "Consulta Fechamento Anual ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgo/FechamentoAnualConsultaList.fxml", (FechamentoAnualConsultaListController controller) -> {
					controller.user = user;
					controller.setServices(new FechamentoAnualService());
		   			controller.updateTableView();
		   			controller.montaForm();
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}
  
	@FXML
	public void onMenuItemConsultaClienteMVRAction() {
		classe = "Consulta Cliente MVR ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgo/ClienteMVRConsultaList.fxml", (ClienteMVRConsultaListController controller) -> {
					controller.user = user;
					controller.setClienteMVRServices(new ClienteService());
					try {
						controller.updateTableView();
						controller.mvrForm();
					} catch (ParseException e) {
						e.printStackTrace();
					}
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}
  
	@FXML
	public void onMenuItemConsultaMaterialMVRAction() {
		classe = "Consulta Material MVR ";
		if (senha != "Ok") {
			temLogar();
		} 
		if (senha == "Ok") {
			if (nivel == 1 || nivel == 9) {
				loadView("/gui/sgo/MaterialMVRConsultaList.fxml", (MaterialMVRConsultaListController controller) -> {
					controller.user = user;
					controller.setMaterialMVRServices(new MaterialService());
					try {
						controller.updateTableView();
						controller.mvrForm();
					} catch (ParseException e) {
						e.printStackTrace();
					}
		});
			} else {
				Alerts.showAlert(null, "Acesso negado ", null, AlertType.WARNING);
			}
		}	
	}
  
	// obrigatoria fun��o x , sem nada; mesmo sem "nada" repassado para atender
	// parametros <consumer> do loadView
	@FXML
	public void onMenuItemSobreAction() {
		if (senha != "Ok") {
			temLogar();
		} else {	
		loadView("/gui/Sobre.fxml", x -> {
		});}
	}

	@FXML
	public void onMenuItemBackUpAction() {
		classe = "BackUp ";
		if (senha != "Ok") {
			temLogar();
		} else {	
			loadView("/gui/copia/CopiaList.fxml", (CopiaSgoController controller) -> {
				controller.user = user;
				controller.setBackUpService(new CopiaService());
				controller.setEntity(new Copia());
				controller.updateTableView();
		});}
	}

	@FXML
	public void onMenuItemRestauraAction() {
		classe = "Restaura ";
		if (senha != "Ok") {
			temLogar();
		} else {	
			if (senha == "Ok") {
				if (nivel == 1 || nivel == 9) {
					loadView("/gui/copia/RestauraList.fxml", (RestauraSgoController controller) -> {
						controller.user = user;
						controller.setRestauraService(new RestauraService());
						controller.setEntity(new Restaura());
						controller.service.zeraAll();
						controller.updateTableView();
					});
				} else {
					Alerts.showAlert(null, "Atenção", "usuário sem acesso", AlertType.WARNING);
				}
				
				}
			}	
		}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		labelUser.setText(user);
	}

	private void temLogar() {
		Alerts.showAlert("Erro login!!!", null, "Tem que logar ", AlertType.ERROR);
	}

	/*
	 * interface consumer <T>, passa a ser fun��o 
	 * generica synchronized garante processo inteiro sem interrup��o
	 */
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
//	private synchronized void loadView(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

// busca o conteudo da variavel no Main via metodo getScene			
			Scene mainScene = MainSgo.getMainScene();
			/*
			 * os filhos da janela Vbox (sobre) inseridos no scrollpane casting vbox
			 * scrollpane trazendo o primeiro reg content getRoot pega o primeiro elemento
			 * da view principal - scrollpane casting scrollpane - getContent
			 */
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

//	busca o primeiro children do vbox da janela principal			
			Node mainMenu = mainVBox.getChildren().get(0);
// limpa os filhos do main vbox
			mainVBox.getChildren().clear();
// adiciona o mainMenu			
			mainVBox.getChildren().add(mainMenu);
// adiciona os filhos do newVbox
			mainVBox.getChildren().addAll(newVBox.getChildren());

// ativar essa fun��o ; retorna o controlador <T> do tipo chamado aqui em cima 
//			na fun��o(Fornecedor, compromis....			
			T controller = loader.getController();
// para executar a a��o -> fun��o accept do consumer			
			initializingAction.accept(controller);
		} catch (IOException e) {
//			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando a página", e.getMessage(), AlertType.ERROR);
		}
	}
	
	@SuppressWarnings("static-access")
	private void createDialogForm(Login log, String absoluteName) {
		try {
 			FXMLLoader loader  = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
// referencia para o controlador = controlador da tela carregada fornListaForm			
			LoginFormController controller = loader.getController();
// injetando passando parametro obj 			
			controller.setLogin(log);
			controller.numEmp = numEmp;
// injetando tb o forn service vindo da tela de formulario fornform
			controller.setLoginService(new LoginService());
// inscrevendo p/ qdo o evento (esse) for disparado executa o metodo -> onDataChangeList...
			controller.subscribeDataChangeListenerMain(this);
//	carregando o obj no formulario (fornecedorFormControl)			
			controller.updateFormData();
			
 			Stage dialogStage = new Stage();
 			dialogStage.setTitle("Logar                                             ");
 			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
//			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", classe + "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
//		catch (ParseException p) {
//			p.printStackTrace();
//		}
 	}

	@Override
	public void onDataChanged() {
		// TODO Auto-generated method stub
		
	}
}